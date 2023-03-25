from email.mime.text import MIMEText
from rest_framework_simplejwt.exceptions import TokenError, InvalidToken, status
from apps.clients.models import Client
from apps.utils.custom_jwt import IsAuthenticatedSimpleCustom,IsAuthenticatedStaffCustom
from .serializers import *
from rest_framework import generics
from django.utils.encoding import smart_str, force_str, smart_bytes
from django.contrib.auth.hashers import make_password, check_password
from drf_spectacular.utils import extend_schema, OpenApiParameter, OpenApiExample
from rest_framework.decorators import action
from rest_framework.response import Response
from django.http import HttpResponsePermanentRedirect
from rest_framework.views import APIView
import os
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
    TokenVerifyView
)

class CustomRedirect(HttpResponsePermanentRedirect):

    allowed_schemes = ['http', 'https']


class CustomInvalidToken(InvalidToken):
    status_code = status.HTTP_403_FORBIDDEN
    default_detail = "Refresh Token is invalid or expired"
    default_code = "Refresh_token_not_valid"


class TokenObtainPairView(TokenObtainPairView):
    
    
    serializer_class = AuthSerializer

    @extend_schema(
        responses={status.HTTP_201_CREATED: SchemaSwaggerAuthSerializer(),}
    )
    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)

        try:
            serializer.is_valid(raise_exception=True)
        except TokenError as e:
            raise InvalidToken(e.args[0])

        return Response({'data': serializer.validated_data}, status=status.HTTP_201_CREATED)


class TokenRefreshView(TokenRefreshView):


    @extend_schema(
        responses={status.HTTP_201_CREATED: SchemaSwaggerAuthSerializer()}
    )
    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)

        try:
            serializer.is_valid(raise_exception=True)
        except TokenError as e:
            raise CustomInvalidToken(e.args[0])

        return Response({'data': serializer.validated_data}, status=status.HTTP_201_CREATED)


class TokenVerifyView(TokenVerifyView):

    
    @extend_schema(
        responses={status.HTTP_204_NO_CONTENT: {},}
    )
    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)

        try:
            serializer.is_valid(raise_exception=True)
        except TokenError as e:
            raise InvalidToken(e.args[0])

        return Response(status=status.HTTP_204_NO_CONTENT)
    
class LogoutView(APIView):

    @extend_schema(
        request=LogoutSerializer,
        responses={status.HTTP_205_RESET_CONTENT: {},}
    )
    def post(self, request, *args, **kwargs):
        try:
            refresh_token = request.data["refresh"]
            token = RefreshToken(refresh_token)
            token.blacklist()

            return Response(status=status.HTTP_205_RESET_CONTENT)
        except Exception as e:
            return Response(status=status.HTTP_400_BAD_REQUEST)

class ChangePasswordView(generics.UpdateAPIView):
    serializer_class = ChangePasswordSerializer
    permission_classes = (IsAuthenticatedSimpleCustom,)
    
    
    def get_object(self, queryset=None):
        obj = self.request.user
        return obj

    
    @extend_schema(
            responses={status.HTTP_200_OK: {'message': 'Password updated successfully',},}
        )
    def put(self, request, *args, **kwargs):
        self.object = self.get_object()
       
        print(self.object.__dict__)
        
        if self.object.__dict__ == {}:
            return Response({"detail": "Authentication credentials were not provided."},status=status.HTTP_401_UNAUTHORIZED)
        
        serializer = self.get_serializer(data=request.data)
        
        if serializer.is_valid():
            if serializer.data.get("new_password","") == serializer.data.get("confirm_new_password",""):
                password_valid = check_password(serializer.data.get("old_password"),self.object.password)
                if not password_valid:
                    return Response({"old_password": ["Wrong password."]}, status=status.HTTP_400_BAD_REQUEST)
                self.object.password = make_password(serializer.data.get("new_password"))
                self.object.save()
                response = {
                    'message': 'Password updated successfully',
                }
                return Response(response, status=status.HTTP_200_OK)      
            else:
                return Response({"details":"Password and confirmation password are not equals"}, status=status.HTTP_400_BAD_REQUEST)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    
class RequestPasswordResetEmail(generics.GenericAPIView):
    serializer_class = ResetPasswordEmailRequestSerializer
    
    
    @extend_schema(
            responses={status.HTTP_200_OK: {'success': 'We have sent you a link to reset your password'},}
        )
    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        
        email = request.data['email']
        try:
            if bool(Client.objects.filter(email=email)):
                user_simple = Client.objects.filter(email=email)
                uidb64 = urlsafe_base64_encode(smart_bytes(user_simple[0].id))

                token = PasswordResetTokenGenerator().make_token(user_simple[0])
                
                current_site = get_current_site(request=request).domain
                relativeLink = reverse('password-reset-complete',kwargs={'uidb64': uidb64,'token': token})

                redirect_url = request.data.get('redirect_url', '')
                absurl = 'http://'+current_site + relativeLink
                email_body = '''
                
                <div  style='text-align:center'> 
                    Hello,
                    <br> Use link below to reset your password  
                    <br>
                    <br> 
                
                    ''' + f'<a href="{absurl}">click here</a>'+'</div>'
                    
                    
                complete_link = MIMEText(email_body ,'html')  
                    
                data = {'to_email': user_simple[0].email,'link': complete_link,
                        'email_subject': 'Reset your passsword'}
        
                Util.send_email(data)
                return Response({'success': 'We have sent you a link to reset your password'}, status=status.HTTP_200_OK)
            else:
                return Response({"detail": "Invalid email"},status=status.HTTP_400_BAD_REQUEST)
        except:
            return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        
    
class SetNewPasswordAPIView(generics.GenericAPIView):
    serializer_class = SetNewPasswordSerializer
    
    @extend_schema(
            parameters=[OpenApiParameter("token", str, OpenApiParameter.PATH),OpenApiParameter("uidb64", str, OpenApiParameter.PATH)],
            responses={status.HTTP_200_OK: {'success': True, 'message': 'Password reset success'},}
        )
    def patch(self, request, uidb64, token):
        try:
            id = smart_str(urlsafe_base64_decode(uidb64))
     
            user = Client.objects.get(pk=id)
     
        except (TypeError, ValueError, OverflowError, User.DoesNotExist):
            user = None
        
        if user is not None and PasswordResetTokenGenerator().check_token(user, token):
            serializer = self.serializer_class(data=request.data)
            if serializer.is_valid():
                try:
                    if request.data.get('password','') == request.data.get('confirm_password',''):
                        user.password = make_password(request.data.get('password',''))
                        user.save() 
                    else:
                        return Response({"details":"Password and confirmation password are not equals"}, status=status.HTTP_400_BAD_REQUEST)
                except Exception as e:
                    raise AuthenticationFailed('The reset link is invalid', 401)
        if not user:
            raise AuthenticationFailed('The reset link is invalid', 401)
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)
        return Response({'success': True, 'message': 'Password reset success'}, status=status.HTTP_200_OK)