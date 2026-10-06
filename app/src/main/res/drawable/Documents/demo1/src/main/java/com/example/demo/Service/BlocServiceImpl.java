package com.example.demo.Service;

import com.example.demo.entity.Bloc;
import com.example.demo.repository.Blocrepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //tjrs hiya w allargsconstr
@AllArgsConstructor

public class BlocServiceImpl implements IBlocService {
  Blocrepository blocrepository;
    @Override
    public List<Bloc> retrieveAllBlocs() {

     List<Bloc> ListB =  blocrepository.findAll();
     return ListB;

    }

    @Override
    public Optional<Bloc> retrieveBloc(Long blocId) {
         return blocrepository.findById(blocId);

    }

    @Override
    public Bloc addBloc(Bloc bloc) {
       return blocrepository.save(bloc);
    }

    @Override
     public void removeBloc(Long blocId) {

        blocrepository.deleteById(blocId);

    }

    @Override
    public Bloc modifyBloc(Bloc bloc) {
     return   blocrepository.save(bloc);
    }
}
