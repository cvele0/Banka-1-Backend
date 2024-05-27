package rs.edu.raf.banka1.model.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListingOptionsDto {
    private String ticker;
    private String optionType;
    private Double strikePrice;
    private String currency;
    private Double impliedVolatility;
    private Integer openInterest;
    private Long expirationDate;
}
