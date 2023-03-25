from django.db import models
from .manager import ClientManager
from django.contrib.auth.models import AbstractBaseUser
import uuid


class Client(AbstractBaseUser):
    
    CLIENT_TIER = (
        ('bronze','bronze'),
        ('silver','silver'),
        ('gold','gold')
    )
    
    id = models.UUIDField(primary_key=True, default=uuid.uuid4)
    email = models.EmailField(null=False, blank=False, max_length=255,unique=True)
    password = models.CharField(null=False, blank=False, max_length=255)
    is_verified = models.BooleanField(default=False)
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    tier = models.CharField(max_length=250, choices=CLIENT_TIER, default='bronze')
    name = models.CharField(blank=True, max_length=255)
    phone = models.CharField(blank=True, max_length=255)
    rg = models.CharField(blank=True, max_length=255)
    cpf = models.CharField(blank=True, max_length=255)
    address = models.CharField(blank=True, max_length=255)

    
    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['password']
    
    
    objects = ClientManager()
    
    def __str__(self):
        return self.email

    
    class Meta:
        db_table = 'clients'
