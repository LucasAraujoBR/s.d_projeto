from rest_framework import serializers
from apps.clients.models import Client


class ClientSeralizer(serializers.ModelSerializer):


    class Meta:
        model = Client
        exclude = ('last_login','is_verified','is_active','is_staff',)
        read_only_fields = (
            'id',
            'created_at',
            'updated_at',
            'deleted_at',
        )
        
class ClientSeralizerList(serializers.ModelSerializer):
 

    class Meta:
        model = Client
        exclude = ('password',)
        read_only_fields = (
            'id',
            'created_at',
            'updated_at',
            'deleted_at',
        )
        
class ClientSeralizerUpdate(serializers.ModelSerializer):
 

    class Meta:
        model = Client
        exclude = ('password',)
        #fields = ("__all__")
        read_only_fields = (
            'id',
            'created_at',
            'updated_at',
            'deleted_at',
        )



class SchemaSwaggerResponseClients(serializers.Serializer):
    data = ClientSeralizer()


class ResponsePostSerializerDefault(serializers.Serializer):
    id = serializers.UUIDField()

class SwaggerErrorDefault(serializers.Serializer):
    errors = serializers.Serializer()


class SchemaSaggerListClients(serializers.Serializer):
    data = ClientSeralizerList(many=True)
    count = serializers.IntegerField()
    total_pages = serializers.IntegerField()
    next_page = serializers.CharField()
    previous_page = serializers.CharField()

