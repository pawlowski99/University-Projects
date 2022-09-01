package pl.pawlowski99.gym.service;

import org.springframework.stereotype.Service;
import pl.pawlowski99.gym.domain.Guide;
import pl.pawlowski99.gym.domain.Workout;
import pl.pawlowski99.gym.repository.GuideRepository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class GuideService {
    private final GuideRepository guideRepository;

    public GuideService(GuideRepository guideRepository) {
        this.guideRepository = guideRepository;
    }

    public Guide saveGuide(Guide guide){
        return guideRepository.save(guide);
    }

    public Guide saveGuide(Guide guide, Long id){
        guide.setId(id);
        return guideRepository.save(guide);
    }

    public Optional<Guide> getGuideById (Long id){
        return guideRepository.findById(id);
    }

    public Iterable<Guide> getAllGuides(){
        return guideRepository.findAll();
    }

    public Map<Long, String> getAllGuidesIdsAndNames(){

        List<Long> allIds = StreamSupport.stream(guideRepository.findAll().spliterator(), false)
                .map(Guide::getId)
                .collect(Collectors.toList());

        List<String> allNames = StreamSupport.stream(guideRepository.findAll().spliterator(), false)
                .map(Guide::getName)
                .collect(Collectors.toList());

        Map<Long, String> maps = new HashMap<>();

        for (int i = 0; i < allIds.size(); i++) {
            maps.put(allIds.get(i),allNames.get(i));
        }

        return maps;
    }


    public void deleteGuideById(Long id){
        guideRepository.deleteById(id);
    }

}
