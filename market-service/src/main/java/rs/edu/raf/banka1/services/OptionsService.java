package rs.edu.raf.banka1.services;

import rs.edu.raf.banka1.model.OptionsModel;
import rs.edu.raf.banka1.model.dtos.ListingOptionsDto;

import java.util.List;
import java.util.Optional;

public interface OptionsService {
    List<ListingOptionsDto> getOptionsByTicker(String ticker);
    List<OptionsModel> getAllOptions();
    Optional<OptionsModel> findById(Long id);
    Optional<List<OptionsModel>> findByExpirationDate(Long expirationDate);
}
