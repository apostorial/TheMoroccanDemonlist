from django import forms
from django.contrib.auth.forms import UserCreationForm
from .models import Player
from region.models import Region
from django_recaptcha.fields import ReCaptchaField
from django.contrib.auth.forms import PasswordResetForm
from django.contrib.auth.forms import SetPasswordForm

class PlayerCreationForm(UserCreationForm):
    captcha = ReCaptchaField()
    email = forms.EmailField(required=True, widget=forms.EmailInput(attrs={
        'class': 'form-control',
        'id': 'email',
    }))
    region = forms.ModelChoiceField(queryset=Region.objects.all(), required=True, widget=forms.Select(attrs={
        'class': 'form-control',
        'id': 'region',
    }))

    class Meta:
        model = Player
        fields = ('username', 'email', 'region', 'password1', 'password2')

class LoginForm(forms.Form):
    captcha = ReCaptchaField()
    username = forms.CharField(max_length=255, required=True,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'id': 'username',
            'placeholder': 'Username'
        })
    )
    password = forms.CharField(required=True,
        widget=forms.PasswordInput(attrs={
            'class': 'form-control',
            'id': 'password',
            'placeholder': 'Password'
        })
    )

    class Meta:
        model = Player
        fields = ('username', 'password')

class CustomPasswordResetForm(PasswordResetForm):
    captcha = ReCaptchaField()

class CustomPasswordResetConfirmForm(SetPasswordForm):
    captcha = ReCaptchaField()

class SocialPlatformForm(forms.ModelForm):
    discord = forms.CharField(max_length=255, required=False,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'id': 'discord',
            'placeholder': 'Your Discord username'
        })
    )
    youtube = forms.CharField(max_length=255, required=False,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'id': 'youtube',
            'placeholder': 'Your YouTube channel'
        })
    )
    twitch = forms.CharField(max_length=255, required=False,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'id': 'twitch',
            'placeholder': 'Your Twitch channel'
        })
    )
    twitter = forms.CharField(max_length=255, required=False,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'id': 'twitter',
            'placeholder': 'Your Twitter profile'
        })
    )
    class Meta:
        model = Player
        fields = ['discord', 'youtube', 'twitch', 'twitter']

class PlayerSearchForm(forms.Form):
    query = forms.CharField(max_length=255)