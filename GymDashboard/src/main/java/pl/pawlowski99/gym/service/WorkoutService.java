package pl.pawlowski99.gym.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.pawlowski99.gym.domain.Muscle;
import pl.pawlowski99.gym.domain.Workout;
import pl.pawlowski99.gym.repository.WorkoutRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Workout saveWorkout(Workout workout){
        return workoutRepository.save(workout);
    }

    public Workout saveWorkout(Workout workout, Long id){
        workout.setId(id);
        return workoutRepository.save(workout);
    }

    public Optional<Workout> getWorkoutById (Long id){
        return workoutRepository.findById(id);
    }

    public Workout getFirstPlannedWorkoutByWorkoutDate (LocalDate date){
        List<Workout> ws = workoutRepository.findByWorkoutDateAndWorkoutStatus(date, 1);

        if(ws.size()>0){
            return ws.get(0);
        }

        return null;
    }

    public Iterable<Workout> getAllWorkouts(){
        return workoutRepository.findAll();
    }

    public Map<Long, String> getAllWorkoutsIdsAndNames(){


        List<Long> allIds = StreamSupport.stream(workoutRepository.findAll().spliterator(), false)
                .map(Workout::getId)
                .collect(Collectors.toList());

        List<String> allNames = StreamSupport.stream(workoutRepository.findAll().spliterator(), false)
                .map(Workout::getName)
                .collect(Collectors.toList());

        Map<Long, String> workouts = new HashMap<>();

        for (int i = 0; i < allIds.size(); i++) {
            workouts.put(allIds.get(i),allNames.get(i));
        }

        return workouts;
    }

    public List<Workout> getAllPlannedUserWorkouts(String username, Character Sorting) {
        Sort sort = Sort.by(Sort.Direction.DESC, "workoutDate");
        if(Sorting == 'A'){
            sort = Sort.by(Sort.Direction.ASC, "workoutDate");
        }
        return workoutRepository.findByUserUsernameAndWorkoutStatus(username,1, sort);
    }
    public List<Workout> getAllCompletedUserWorkouts(String username, Character Sorting) {
        Sort sort = Sort.by(Sort.Direction.DESC, "workoutDate");
        if(Sorting == 'A'){
            sort = Sort.by(Sort.Direction.ASC, "workoutDate");
        }
        return workoutRepository.findByUserUsernameAndWorkoutStatus(username,0, sort);
    }

    public List<Workout> getAllUserWorkouts(String username, Character Sorting) {
        Sort sort = Sort.by(Sort.Direction.DESC, "workoutDate");
        if(Sorting == 'A'){
            sort = Sort.by(Sort.Direction.ASC, "workoutDate");
        }
        return workoutRepository.findByUserUsername(username, sort);
    }

    public void deleteWorkoutById(Long id){
        workoutRepository.deleteById(id);
    }

    public List<Workout> getCompletedWorkouts() {
        Sort sort = Sort.by(Sort.Direction.DESC, "workoutDate");
        return workoutRepository.findByWorkoutStatus(0, sort);
    }

    public List<Workout> getPlannedWorkouts() {
        Sort sort = Sort.by(Sort.Direction.ASC, "workoutDate");
        return workoutRepository.findByWorkoutStatus(1, sort);
    }

    public List<Integer> getWorkoutsPerMonth() {
        List<Integer> workouts = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0);

        int currentYear = Year.now().getValue();

        for(int i=0;i<12;i++){
            int finalI = i+1;
            int count = (int)StreamSupport.stream(workoutRepository.findAll().spliterator(), false)
                    .filter(w -> w.getYear() == currentYear).filter(w -> w.getMonth() == finalI).filter(w -> w.getWorkoutStatus() == 0).count();

            workouts.set(i, count);
        }

        return workouts;
    }

    public List<Integer> getWorkoutsPerMonthByUsername(String username) {
        List<Integer> workouts = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0);

        int currentYear = Year.now().getValue();

        for(int i=0;i<12;i++){
            int finalI = i+1;
            int count = (int)StreamSupport.stream(workoutRepository.findByUserUsername(username).spliterator(), false)
                    .filter(w -> w.getYear() == currentYear).filter(w -> w.getMonth() == finalI).filter(w -> w.getWorkoutStatus() == 0).count();

            workouts.set(i, count);
        }

        return workouts;
    }

    public Map<String,  Long> getMostWorkoutsInAMonthForUsername(String username) {
        Map<String, Long> allMonths = StreamSupport.stream(workoutRepository.findByUserUsername(username).spliterator(), false)
                .filter(w -> w.getWorkoutStatus() == 0)
                .collect(Collectors.groupingBy(Workout::getMonthAndYear, Collectors.counting()));


        Map<String, Long> map = new HashMap<>();
        Long max = 0L;
        String month = "00-0000";

        if(allMonths.size()>0) {
            for (Map.Entry<String, Long> entry : allMonths.entrySet()) {
                String k = entry.getKey();
                Long v = entry.getValue();

                if (v > max) {
                    month = k;
                    max = v;
                }
            }
        }
        map.put(month,max);
        return map;
    }

    public Workout getFirstWorkoutByUsername (String username){
        Sort sort = Sort.by(Sort.Direction.ASC, "workoutDate");

        List<Workout> ws = workoutRepository.findByUserUsernameAndWorkoutStatus(username, 0, sort);

        if (ws.size()> 0){
            return workoutRepository.findByUserUsernameAndWorkoutStatus(username, 0, sort).get(0);
        }
        else{
            return null;
        }
    }


    public Workout getLastWorkoutByUsername (String username){
        Sort sort = Sort.by(Sort.Direction.DESC, "workoutDate");

        List<Workout> ws = workoutRepository.findByUserUsernameAndWorkoutStatus(username, 0, sort);

        if (ws.size()> 0){
            return workoutRepository.findByUserUsernameAndWorkoutStatus(username, 0, sort).get(0);
        }
        else{
            return null;
        }
    }

    public Workout getWorkoutWithMostExercisesByUsername(String username){
        Sort sort = Sort.by(Sort.Direction.DESC, "workoutDate");

        List<Workout> ws = workoutRepository.findByUserUsernameAndWorkoutStatus(username, 0, sort);

        if (ws.size()> 0){
            Workout w1 = workoutRepository.findByUserUsernameAndWorkoutStatus(username, 0, sort).get(0);

            for (Workout var : ws) {
               if(var.getExercises().size() > w1.getExercises().size()){
                   w1 = var;
               }
            }

            return w1;
        }
        else{
            return null;
        }
    }


}




