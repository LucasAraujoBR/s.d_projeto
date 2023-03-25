from django.urls import path
from .viewsets import *
from django.conf import settings
from django.conf.urls.static import static

# outras rotas do seu projeto

urlpatterns = [
    path('login/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('verify/', TokenVerifyView.as_view(), name='token_verify'),
    path('logout/', LogoutView.as_view(), name='token_logout'),
    path('change_password/', ChangePasswordView.as_view(), name='token_change_password'),
    path('request-reset-email/', RequestPasswordResetEmail.as_view(), name="request-reset-email"),
    path('password-reset/<uidb64>/<token>/', SetNewPasswordAPIView.as_view(), name='password-reset-complete')
]