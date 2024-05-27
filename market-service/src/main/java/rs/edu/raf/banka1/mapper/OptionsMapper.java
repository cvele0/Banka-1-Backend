package rs.edu.raf.banka1.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.banka1.model.OptionsModel;
import rs.edu.raf.banka1.model.dtos.ListingOptionsDto;

@Component
public class OptionsMapper {
    public ListingOptionsDto toDto(OptionsModel optionsModel) {
        ListingOptionsDto optionsDto = new ListingOptionsDto();
        optionsDto.setOptionType(optionsModel.getOptionType());
        optionsDto.setCurrency(optionsModel.getCurrency());
        optionsDto.setTicker(optionsModel.getTicker());
        optionsDto.setOpenInterest(optionsModel.getOpenInterest());
        optionsDto.setStrikePrice(optionsModel.getStrikePrice());
        optionsDto.setExpirationDate(optionsModel.getExpirationDate());
        optionsDto.setImpliedVolatility(optionsModel.getImpliedVolatility());
        return optionsDto;
    }

    // Not used
    public OptionsModel optionsDtoToOptionsModel(ListingOptionsDto optionsDto) {
        OptionsModel optionsModel = new OptionsModel();
        optionsModel.setOptionType(optionsDto.getOptionType());
        optionsModel.setCurrency(optionsDto.getCurrency());
        optionsModel.setTicker(optionsDto.getTicker());
        optionsModel.setOpenInterest(optionsDto.getOpenInterest());
        optionsModel.setStrikePrice(optionsDto.getStrikePrice());
        optionsModel.setExpirationDate(optionsDto.getExpirationDate());
        optionsModel.setImpliedVolatility(optionsDto.getImpliedVolatility());
        return optionsModel;
    }
}
