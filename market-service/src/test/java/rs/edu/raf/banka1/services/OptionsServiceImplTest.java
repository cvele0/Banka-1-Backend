package rs.edu.raf.banka1.services;

import org.junit.jupiter.api.BeforeEach;
import rs.edu.raf.banka1.mapper.OptionsMapper;
//import rs.edu.raf.banka1.model.dtos.ListingOptionsDto;
import rs.edu.raf.banka1.repositories.OptionsRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class OptionsServiceImplTest {
    private OptionsServiceImpl optionsService;
    private OptionsRepository optionsRepository;
    private OptionsMapper optionsMapper;

    @BeforeEach
    public void setUp() {
        optionsRepository = mock(OptionsRepository.class);
        optionsMapper = new OptionsMapper();
        optionsService = new OptionsServiceImpl(optionsRepository, optionsMapper);
    }

//    @Test
//    public void getOptionsByTickerTest_Success(){
//        ListingOptions option1 = new ListingOptions();
//        option1.setTicker("AAPL");
//        ListingOptions option2 = new ListingOptions();
//        option2.setTicker("AAPL");
//
//
//        when(optionsRepository.findByTicker("AAPL")).thenReturn(Optional.of(List.of(option1, option2)));
//
//        List<ListingOptionsDto> options = optionsService.getOptionsByTicker("AAPL");
//        verify(optionsRepository, times(1)).findByTicker("AAPL");
//        Assertions.assertEquals(2, options.size());
//    }

//    @Test
//    public void getOptionsByTickerTest_EmptyDB_noCrumb(){
//        when(optionsRepository.findByTicker("AAPL")).thenReturn(Optional.of(List.of()));
//
//        List<ListingOptionsDto> options = optionsService.getOptionsByTicker("AAPL");
//        verify(optionsRepository, times(1)).findByTicker("AAPL");
//        Assertions.assertEquals(0, options.size());
//    }
}
