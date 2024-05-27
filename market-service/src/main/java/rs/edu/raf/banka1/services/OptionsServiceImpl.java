package rs.edu.raf.banka1.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import rs.edu.raf.banka1.mapper.OptionsMapper;
import rs.edu.raf.banka1.model.OptionsModel;
import rs.edu.raf.banka1.model.dtos.ListingOptionsDto;
import rs.edu.raf.banka1.repositories.OptionsRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OptionsServiceImpl implements OptionsService {
    private OptionsRepository optionsRepository;
    private OptionsMapper optionsMapper;

    public OptionsServiceImpl(OptionsRepository optionsRepository,
                              OptionsMapper optionsMapper) {
        this.optionsRepository = optionsRepository;
        this.optionsMapper = optionsMapper;
    }

    @Override
    @Cacheable(value = "optionsServiceOptionsByTicker", key = "#ticker")
    public List<OptionsModel> getAllOptions() {
        return optionsRepository.findAll();
    }

    @Override
    public Optional<OptionsModel> findById(Long id) {
        return optionsRepository.findById(id);
    }

    @Override
    public Optional<List<OptionsModel>> findByExpirationDate(Long expirationDate) {
        return this.optionsRepository.findByExpirationDate(expirationDate);
    }

    @Override
    public List<ListingOptionsDto> getOptionsByTicker(String ticker) {
        List<ListingOptionsDto> options = this.optionsRepository.findByTicker(ticker).map(optionsModels ->
                        optionsModels.stream()
                                .map(optionsMapper::toDto)
                                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        if(options.isEmpty()) {
            // optionally fetch options
        }
        return options;
    }
}



