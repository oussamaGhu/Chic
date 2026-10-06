import { Test, TestingModule } from '@nestjs/testing';
import { AssembleController } from './assemble.controller';
import { AssembleService } from './assemble.service';

describe('AssembleController', () => {
  let controller: AssembleController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [AssembleController],
      providers: [AssembleService],
    }).compile();

    controller = module.get<AssembleController>(AssembleController);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });
});
