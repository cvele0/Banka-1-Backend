package rs.edu.raf.banka1.dtos.market_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingOptionsDto extends ListingBaseDto {
    private String ticker;
    private String optionType;
    private Double strikePrice;
    private String currency;
    private Double impliedVolatility;
    private Integer openInterest;
    private Long expirationDate;
}
