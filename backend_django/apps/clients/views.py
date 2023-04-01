from django.contrib.auth.views import LoginView
from django.views.generic import ListView
class Login(LoginView):
    template_name = 'login.html'
    
    
class Onboard(LoginView):
    template_name = 'onboard.html'
