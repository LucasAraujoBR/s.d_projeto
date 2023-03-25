from rest_framework_simplejwt.authentication import JWTAuthentication
from rest_framework.permissions import IsAuthenticated
from apps.clients.models import Client
from drf_spectacular.extensions import OpenApiAuthenticationExtension

class IsAuthenticatedStaffCustom(IsAuthenticated,):

    def has_permission(self, request, view):
        return bool(request.user.is_staff)

class IsAuthenticatedSimpleCustom(IsAuthenticated,):

    def has_permission(self, request, view):
        if str(request.user) == 'AnonymousUser':
            return False
        else:
            return True


class JWTCustom(JWTAuthentication):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.user_model = Client
