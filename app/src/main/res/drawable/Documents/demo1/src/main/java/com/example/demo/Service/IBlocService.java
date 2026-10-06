package com.example.demo.Service;

import com.example.demo.entity.Bloc;

import java.util.List;
import java.util.Optional;

public interface IBlocService {
    public List<Bloc> retrieveAllBlocs();

    public Optional<Bloc> retrieveBloc(Long blocId);

   public Bloc addBloc(Bloc bloc);

   public void removeBloc(Long blocId);

   public Bloc modifyBloc(Bloc bloc);

}
