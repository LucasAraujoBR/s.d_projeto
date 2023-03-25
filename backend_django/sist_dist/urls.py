"""sist_dist URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path,include
from drf_spectacular.views import SpectacularAPIView, SpectacularRedocView, SpectacularSwaggerView

from django.conf import settings
from django.conf.urls.static import static

from apps.clients.views import Login
app_name = 'backend'
urlpatterns = [
    path('admin/', admin.site.urls),
    path('hotel/', SpectacularAPIView.as_view(), name='docs'),
    path('docs/', SpectacularSwaggerView.as_view(url_name='docs'), name='swagger-ui'),
    path('auth/', include('apps.auth_system.api.urls'), name='Auth'),
    path('client/', include('apps.clients.api.clients.urls'), name='Clients'),
    path('login/', Login.as_view(), name='Login'),

]
