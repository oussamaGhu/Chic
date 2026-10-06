import { Controller, Get, Post, Body, Patch, Param, Delete, NotFoundException } from '@nestjs/common';
import { RequestService } from './request.service';
import { CreateRequestDto } from './dto/create-request.dto';
import { UpdateRequestDto } from './dto/update-request.dto';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Request }  from 'src/user/schema/request.schema'; // Ensure to import the Request model/schema

@ApiTags('Request')
@Controller('request')
export class RequestController {
  constructor(private readonly requestService: RequestService) {}

  @Post()
  @ApiOperation({ summary: 'Create a new request' })
  @ApiResponse({ status: 201, description: 'Request successfully created', type: Request })
  async create(@Body() createRequestDto: CreateRequestDto): Promise<Request> {
    // Check if a request already exists with the same unique identifier
    const existingRequest = await this.requestService.findOneByUniqueFields(createRequestDto.clientId,createRequestDto.itemId);
    
    if (existingRequest) {
      throw new NotFoundException('Request already sent');
    }
    
    return this.requestService.create(createRequestDto);
  }

  @Get()
  @ApiOperation({ summary: 'Get all requests' })
  @ApiResponse({ status: 200, description: 'Successfully retrieved all requests', type: [Request] })
  findAll(): Promise<Request[]> {
    return this.requestService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a single request by ID' })
  @ApiResponse({ status: 200, description: 'Successfully retrieved the request', type: Request })
  @ApiResponse({ status: 404, description: 'Request not found' })
  findOne(@Param('id') id: string): Promise<Request> {
    return this.requestService.findOne(id);
  }

  @Patch(':id')
  @ApiOperation({ summary: 'Update a request' })
  @ApiResponse({ status: 200, description: 'Request successfully updated', type: Request })
  @ApiResponse({ status: 404, description: 'Request not found' })
  update(@Param('id') id: string, @Body() updateRequestDto: UpdateRequestDto): Promise<Request> {
    return this.requestService.update(id, updateRequestDto);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete a request' })
  @ApiResponse({ status: 200, description: 'Request successfully deleted' })
  @ApiResponse({ status: 404, description: 'Request not found' })
  remove(@Param('id') id: string): Promise<Request> {
    return this.requestService.remove(id);
  }
}