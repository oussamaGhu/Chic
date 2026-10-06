import { Test, TestingModule } from '@nestjs/testing';
import { AssembleService } from './assemble.service';

describe('AssembleService', () => {
  let service: AssembleService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [AssembleService],
    }).compile();

    service = module.get<AssembleService>(AssembleService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });
});
