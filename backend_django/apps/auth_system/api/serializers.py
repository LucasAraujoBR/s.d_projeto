from rest_framework import serializers
from apps.clients.models import Client
from rest_framework.exceptions import AuthenticationFailed
from django.contrib.auth.hashers import make_password, check_password
from rest_framework_simplejwt.tokens import RefreshToken
from django.contrib.auth.tokens import PasswordResetTokenGenerator
from django.utils.encoding import smart_str, force_str, smart_bytes, DjangoUnicodeDecodeError
from django.utils.http import urlsafe_base64_decode, urlsafe_base64_encode
from apps.utils.email_model import Util
from django.contrib.sites.shortcuts import get_current_site
from django.urls import reverse
from rest_framework.response import Response
from rest_framework import generics, status
from django.contrib.auth.models import User

from apps.clients.api.clients.serializers import ClientSeralizer



class ResetPasswordEmailRequestSerializer(serializers.Serializer):
    email = serializers.EmailField(min_length=2)

    #redirect_url = serializers.CharField(max_length=500, required=False)

    class Meta:
        fields = ['email']



class LogoutSerializer(serializers.Serializer):
    refresh = serializers.CharField(required=True)
    
    class Meta:
        fields = ['refresh']


class SetNewPasswordSerializer(serializers.Serializer):
    password = serializers.CharField(
        min_length=6, max_length=68, write_only=True)
    confirm_password = serializers.CharField(
        min_length=6, max_length=68, write_only=True)

    class Meta:
        fields = ['password','confirm_password',]

class ResetPasswordSerializer(serializers.Serializer):
    email = serializers.EmailField(min_length=2)
    
    class Meta:
        
        fields = ['email']
        
    def validate(self, attrs):
    
        email = attrs.get('email','')
        
        return super().validate(attrs)



class ChangePasswordSerializer(serializers.Serializer):
    model = Client
    old_password = serializers.CharField(required=True)
    new_password = serializers.CharField(required=True)
    confirm_new_password = serializers.CharField(required=True)


class AuthSerializer(serializers.Serializer):
    email = serializers.EmailField()
    password =serializers.CharField()
    
    class Meta:
        fields = ('email','password')

    def get_tokens_for_user(self, user, userSerialized):
        refresh = RefreshToken.for_user(user)
        # print(userSerialized)

        return {
            'user': userSerialized,
            'access': str(refresh.access_token),
            'refresh': str(refresh),
        }

    def validate(self, attrs):
        email = attrs.get('email', '')
        password = attrs.get('password', '')

        current_users_hashed = Client.objects.filter(email=email)

        def valide_hash_custom(current_credentials_hashed):
            for key in current_credentials_hashed:
                if check_password(password,key.password):
                    return True
            return False

        user = valide_hash_custom(current_users_hashed)
        print(user)

        
        if not user:
            raise AuthenticationFailed('Invalid users')
       
        obj = Client.objects.get(email=attrs.get('email', ''))
        userSerialized = ClientSeralizer(obj, data=self.initial_data)

        if userSerialized.is_valid():
            return self.get_tokens_for_user(obj, userSerialized.data)

       



class PostResponseJWTSerializer(serializers.Serializer):
    access = serializers.CharField()
    refresh = serializers.CharField()

class SchemaSwaggerAuthSerializer(serializers.Serializer):
    data = PostResponseJWTSerializer()