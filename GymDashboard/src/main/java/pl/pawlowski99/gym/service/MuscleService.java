package pl.pawlowski99.gym.service;

import org.springframework.stereotype.Service;
import pl.pawlowski99.gym.domain.Muscle;
import pl.pawlowski99.gym.repository.MuscleRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MuscleService {
    private final MuscleRepository muscleRepository;

    public MuscleService(MuscleRepository muscleRepository) {
        this.muscleRepository = muscleRepository;
    }

    public Muscle saveMuscle(Muscle muscle){
        return muscleRepository.save(muscle);
    }


    public Muscle saveMuscle(Muscle muscle, Long id){
        muscle.setId(id);
        return muscleRepository.save(muscle);
    }

    public Optional<Muscle> getMuscleById (Long id){
        return muscleRepository.findById(id);
    }

    public Iterable<Muscle> getAllMuscles(){
        return muscleRepository.findAll();
    }

    public void deleteMuscleById(Long id){
        muscleRepository.deleteById(id);
    }

}
