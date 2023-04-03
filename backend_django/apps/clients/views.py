from django.contrib.auth.views import LoginView


class Login(LoginView):
    template_name = 'login.html'
    
    
class Onboard(LoginView):
    template_name = 'onboard.html'
    
class Menu(LoginView):
    template_name = 'menu.html'
