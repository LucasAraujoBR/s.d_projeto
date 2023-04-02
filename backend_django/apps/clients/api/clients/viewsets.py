from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import smtplib
from apps.clients.models import Client
from apps.clients.api.clients.serializers import *
from rest_framework import status
from rest_framework.viewsets import ModelViewSet
from apps.utils.custom_jwt import IsAuthenticatedStaffCustom, IsAuthenticatedSimpleCustom
from django.contrib.auth.hashers import make_password
from django.utils.decorators import method_decorator
from drf_spectacular.utils import extend_schema, OpenApiParameter, OpenApiExample
from drf_spectacular.types import OpenApiTypes
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
from rest_framework.permissions import AllowAny
from rest_framework.decorators import action
import Pyro4
from Pyro4 import Proxy


class ClientViewSet(ModelViewSet):
    
    queryset = Client.objects.all()
    serializer_class = ClientSeralizer

    permission_classes = (AllowAny,)

    custom_response_models: dict = {
        'create': ResponsePostSerializerDefault,
        
    }
    
    def apply_owner(self):
        data = self.request.data
        #data.update({'id': self.request.user.id})
        return data

    def get_response_model(self, data, *args, **kwargs):
            new_serializer_response = self.custom_response_models.get(self.action, self.serializer_class)
            kwargs.setdefault('context', self.get_serializer_context())

            if isinstance(data, list):
                return new_serializer_response(data, many=True, **kwargs).data 

            elif new_serializer_response:
                data = new_serializer_response(data, **kwargs).data
        
            return {'data': data }
        
    def get_response_model_get(self, data, *args, **kwargs):
            new_serializer_response = self.custom_response_models.get(self.action, ClientSeralizerList)
            kwargs.setdefault('context', self.get_serializer_context())

            if isinstance(data, list):
                return new_serializer_response(data, many=True, **kwargs).data 

            elif new_serializer_response:
                data = new_serializer_response(data, **kwargs).data
        
            return {'data': data }

    
    @extend_schema(responses={
        status.HTTP_201_CREATED : ResponsePostSerializerDefault,
        status.HTTP_400_BAD_REQUEST : SwaggerErrorDefault
        })
    def create(self, request):

        data =  self.request.data
      
        if 'password' in data:
            data['password'] = make_password(data['password'])
        
        obj_client = Client.objects.filter(email=data['email']).first()

        if obj_client:
            return Response({"details": "email already exists in the database"}, status=status.HTTP_400_BAD_REQUEST)
       
        serializer = self.get_serializer(data=data)
  
        serializer.is_valid(raise_exception=True)

        self.perform_create(serializer)
        headers = self.get_success_headers(serializer.data)
        response = self.get_response_model(serializer.data)
        return Response(response, status=status.HTTP_201_CREATED, headers=headers)

    
    @extend_schema(responses={
        status.HTTP_204_NO_CONTENT: '',
        status.HTTP_404_NOT_FOUND: '',        
        status.HTTP_400_BAD_REQUEST : SwaggerErrorDefault
        })
    def update(self, request, *args, **kwargs):
        permission_classes = (IsAuthenticatedSimpleCustom,)
        data =  self.apply_owner()
        
        if request.data.get('password','') != '':
            return Response({"Error":"Cannot change password in this method"},status=status.HTTP_400_BAD_REQUEST)
        if request.data.get('email','') != '':
            obj_client = Client.objects.filter(email=data['email']).first()

            if obj_client:
                return Response({"details": "email already exists in the database"}, status=status.HTTP_400_BAD_REQUEST)
            
        partial = kwargs.pop('partial', False)
      
        instance = self.get_object()
        
        serializer = ClientSeralizerUpdate(instance=instance ,data=data, partial=partial)
        
        serializer.is_valid(raise_exception=True)
        #print(serializer.password)
        self.perform_update(serializer)
        return Response(status=status.HTTP_204_NO_CONTENT)

    @extend_schema(responses={
        status.HTTP_204_NO_CONTENT: '',
        status.HTTP_404_NOT_FOUND: '',        
        status.HTTP_400_BAD_REQUEST : SwaggerErrorDefault
        })
    def partial_update(self, request, *args, **kwargs):
        permission_classes = (IsAuthenticatedSimpleCustom,)
        return super().partial_update(request, *args, **kwargs)


    def retrieve(self, request, *args, **kwargs):
        permission_classes = (IsAuthenticatedSimpleCustom,)
        instance = self.get_object()
        response = self.get_response_model_get(instance)
        return Response(response)

    
    @extend_schema(
        parameters=[
            OpenApiParameter(name='limit', description='desired page limit', required=False, type=int),
            OpenApiParameter(name='page', description='desired page number', required=False, type=int),
            ]
    )
    def list(self, request, *args, **kwargs):
        permission_classes = (IsAuthenticatedSimpleCustom,)
        queryset = self.filter_queryset(self.get_queryset())

        page = self.paginate_queryset(queryset)

        if page is not None:
            response = self.get_response_model_get(page)
            return self.get_paginated_response(response)


        serializer = self.get_serializer(queryset, many=True)
        response = self.get_response_model_get(serializer.data)
      
        
        return Response(response)

    
    
    @action(detail=False, methods=['post'], url_path='send_email', url_name='send_email')
    def email_send(self, request, *args, **kwargs):
        if 'email' in request.data and 'descricao' in request.data:  
             
            email_proxy = Proxy("PYRO:Email@localhost:8080")
            email_proxy.sending_email(receiver=request.data['email'],body=request.data['descricao'])
            return Response(status=status.HTTP_200_OK)
        
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)
       
       