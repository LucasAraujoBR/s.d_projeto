from rest_framework.pagination import PageNumberPagination
from rest_framework.response import Response
from collections import OrderedDict
import math



class CustomPagination(PageNumberPagination):
    page_size_query_param = 'limit'

    def get_amount_pages(self):
        page_size = self.request.GET.get(self.page_size_query_param, self.page_size)
        
        if not str(page_size).isnumeric() or  int(page_size) == 0:
            return 1

        page_size =int(page_size)
        registers = self.page.paginator.count
        pages = math.ceil(registers / page_size)

        return pages

    def get_paginated_response(self, data):
        return Response(OrderedDict([
            ('count', self.page.paginator.count),
            ('total_pages', self.get_amount_pages()),
            ('next', self.get_next_link()),
            ('previous', self.get_previous_link()),
            ('data', data)
        ]))
