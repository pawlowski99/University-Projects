package pl.pawlowski99.gym.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.pawlowski99.gym.domain.Exercise;
import pl.pawlowski99.gym.domain.Guide;
import pl.pawlowski99.gym.domain.Muscle;
import pl.pawlowski99.gym.domain.Workout;
import pl.pawlowski99.gym.repository.ExerciseRepository;
import pl.pawlowski99.gym.repository.MuscleRepository;
import pl.pawlowski99.gym.repository.WorkoutRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository muscleRepository;
    private final WorkoutRepository workoutRepository;


    public ExerciseService(ExerciseRepository exerciseRepository, MuscleRepository muscleRepository, WorkoutRepository workoutRepository) {
        this.exerciseRepository = exerciseRepository;
        this.muscleRepository = muscleRepository;
        this.workoutRepository = workoutRepository;
    }

    public Exercise saveExercise(Exercise exercise){
        return exerciseRepository.save(exercise);
    }

    public Exercise saveExercise(Exercise exercise, Long id){
        exercise.setId(id);
        return exerciseRepository.save(exercise);
    }

    public Optional<Exercise> getExerciseById (Long id){
        return exerciseRepository.findById(id);
    }

    public Iterable<Exercise> getAllExercises(){
        return exerciseRepository.findAll();
    }

    public void deleteExerciseById(Long id){
        exerciseRepository.deleteById(id);
    }

    public List<Double> getWeightsPercentageUsage() {
        List<Double> weights = Arrays.asList(0.0,0.0);

        double count = StreamSupport.stream(exerciseRepository.findAll().spliterator(), false)
                .filter(e -> e.getWeights() > 0).count();

        double all = StreamSupport.stream(exerciseRepository.findAll().spliterator(), false).count();

        double equipment = (count/all)*100;

        weights.set(0, equipment );
        weights.set(1, 100-equipment);

        return weights;
    }

    public List<Double> getWeightsPercentageUsageInMonthByUsername(String username) {
        List<Double> weights = Arrays.asList(0.0,0.0);

        double count = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .filter(e -> e.getWorkout().getWorkoutStatus() == 0)
                .filter(e -> e.getWorkout().getWorkoutDate().getMonth().getValue() == LocalDate.now().getMonth().getValue())
                .filter(e -> e.getWeights() > 0)
                .count();

        double all = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .filter(e -> e.getWorkout().getWorkoutStatus() == 0)
                .filter(e -> e.getWorkout().getWorkoutDate().getMonth().getValue() == LocalDate.now().getMonth().getValue())
                .count();

        double equipment =  Math.round((count/all)*100);

        weights.set(0, equipment );
        weights.set(1, 100-equipment);

        return weights;
    }

    public List<Double> getWeightsPercentageUsageInYearByUsername(String username) {
        List<Double> weights = Arrays.asList(0.0,0.0);

        double count = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .filter(e -> e.getWorkout().getWorkoutStatus() == 0)
                .filter(e -> e.getWorkout().getWorkoutDate().getYear() == LocalDate.now().getYear())
                .filter(e -> e.getWeights() > 0).count();

        double all = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .filter(e -> e.getWorkout().getWorkoutStatus() == 0)
                .filter(e -> e.getWorkout().getWorkoutDate().getYear() == LocalDate.now().getYear())
                .count();

        double equipment = Math.round((count/all)*100);

        weights.set(0, equipment );
        weights.set(1, 100-equipment);

        return weights;
    }

    public Map<String, Long> getMuscleIntMByUsername(String username){

        List<Muscle> allMuscles = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .filter(e -> e.getWorkout().getWorkoutDate().getMonth().getValue() == LocalDate.now().getMonth().getValue())
                .map(Exercise::getGuide)
                .map(Guide::getMuscle)
                .collect(Collectors.toList());

        List<String> allNames = StreamSupport.stream(muscleRepository.findAll().spliterator(), false)
                .map(Muscle::getName)
                .distinct()
                .collect(Collectors.toList());
//

        Map<String,Long> maps = new HashMap<>();

        for (String var : allNames)
        {
            maps.put(var, 0L);
        }

//        List<Long> allValues = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
//                .map(Guide::getId)
//                .collect(Collectors.toList());

        Map<String, Long> countForName = allMuscles.stream()
                .collect(Collectors.groupingBy(Muscle::getName, Collectors.counting()));

//        Map<String,Long> maps = new HashMap<>();
//
//        for (int i = 0; i < allNames.size(); i++) {
//            maps.put(allNames.get(i),allValues.get(i));
//        }


        Map<String,Long> map3 = new HashMap<>();
        map3.putAll(maps);
        map3.putAll(countForName);

        return map3;
    }

    public Map<String, Long> getMuscleIntYByUsername(String username){

        List<Muscle> allMuscles = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .filter(e -> e.getWorkout().getWorkoutDate().getYear() == LocalDate.now().getYear())
                .map(Exercise::getGuide)
                .map(Guide::getMuscle)
                .collect(Collectors.toList());

        List<String> allNames = StreamSupport.stream(muscleRepository.findAll().spliterator(), false)
                .map(Muscle::getName)
                .distinct()
                .collect(Collectors.toList());

        Map<String,Long> maps = new HashMap<>();

        for (String var : allNames)
        {
            maps.put(var, 0L);
        }

        Map<String, Long> countForName = allMuscles.stream()
                .collect(Collectors.groupingBy(Muscle::getName, Collectors.counting()));

        Map<String,Long> map3 = new HashMap<>();
        map3.putAll(maps);
        map3.putAll(countForName);

        return map3;
    }


    public Integer getHeaviestByUser(String username){
        List<Integer> allExes = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .map(Exercise::getWeights)
                .collect(Collectors.toList());

        Integer max = 0;
        for (Integer var : allExes) {
            if(var > max){
                max = var;
            }
        }
        return max;
    }

    public String getFavExeByUser(String username){
        Map<Guide, Long> allExes = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .collect(Collectors.groupingBy(Exercise::getGuide, Collectors.counting()));

        Iterator<Map.Entry<Guide, Long>> itr = allExes.entrySet().iterator();

        Long maxV = 0L;
        String maxN = "None";

        while(itr.hasNext())
        {
            Map.Entry<Guide, Long> entry = itr.next();

            if(entry.getValue() > maxV){
                maxV = entry.getValue();
                maxN = entry.getKey().getName();
            }
        }
        return maxN;
    }

    public String getMostMuscleByUser(String username){
        Map<String, Long> allMuscles = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .filter(e -> e.getWorkout().getWorkoutDate().getYear() == LocalDate.now().getYear())
                .map(Exercise::getGuide)
                .map(Guide::getMuscle)
                .collect(Collectors.groupingBy(Muscle::getName, Collectors.counting()));

        Long maxV = 0L;
        String maxN = "None";

        Iterator<Map.Entry<String, Long>> itr = allMuscles.entrySet().iterator();

        while(itr.hasNext())
        {
            Map.Entry<String, Long> entry = itr.next();

            if(entry.getValue() > maxV){
                maxV = entry.getValue();
                maxN = entry.getKey();
            }
        }
        return maxN;
    }

    public String getLeastMuscleByUser(String username){
        Map<String, Long> allMuscles = StreamSupport.stream(exerciseRepository.findByWorkoutUserUsername(username).spliterator(), false)
                .filter(e -> e.getWorkout().getWorkoutDate().getYear() == LocalDate.now().getYear())
                .map(Exercise::getGuide)
                .map(Guide::getMuscle)
                .collect(Collectors.groupingBy(Muscle::getName, Collectors.counting()));

        Long minV;
        String maxN = "None";

        Iterator<Map.Entry<String, Long>> itr = allMuscles.entrySet().iterator();

        if(allMuscles.size() > 0){
            minV = (Long) allMuscles.values().toArray()[0];

            while(itr.hasNext())
            {
                Map.Entry<String, Long> entry = itr.next();
                if(entry.getValue() < minV){
                    minV = entry.getValue();
                    maxN = entry.getKey();
                }
            }
        }

        return maxN;
    }
}
