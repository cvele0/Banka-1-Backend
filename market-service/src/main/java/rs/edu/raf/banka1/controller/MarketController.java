package rs.edu.raf.banka1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import rs.edu.raf.banka1.mapper.*;
import rs.edu.raf.banka1.model.*;
import rs.edu.raf.banka1.model.dtos.*;
import rs.edu.raf.banka1.services.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/market")
@Tag(name = "Market", description = "Market API")
//@SecurityRequirement() TODO
@CrossOrigin
@SecurityRequirement(name = "basicScheme")
@SecurityRequirement(name = "Authorization")
public class MarketController {

    private final ExchangeService exchangeService;
    private final ForexService forexService;
    private final FuturesService futuresService;
    private final OptionsService optionsService;

    private final ListingStockService listingStockService;
    private final ListingHistoryMapper listingHistoryMapper;
    private final ForexMapper forexMapper;
    private final StockMapper stockMapper;
    private final FutureMapper futureMapper;
    private final OptionsMapper optionsMapper;

    @Autowired
    public MarketController(ExchangeService exchangeService, ForexService forexService,
                            ListingStockService listingStockService, FuturesService futuresService,
                            ListingHistoryMapper listingHistoryMapper, ForexMapper forexMapper,
                            StockMapper stockMapper, FutureMapper futureMapper, OptionsService optionsService, OptionsMapper optionsMapper) {
        this.exchangeService = exchangeService;
        this.forexService = forexService;
        this.listingStockService = listingStockService;
        this.futuresService = futuresService;
        this.listingHistoryMapper = listingHistoryMapper;
        this.forexMapper = forexMapper;
        this.stockMapper = stockMapper;
        this.futureMapper = futureMapper;
        this.optionsService = optionsService;
        this.optionsMapper = optionsMapper;
    }

    @GetMapping(value = "/exchange", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all exchanges", description = "Returns all exchanges",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class,
                                    subTypes = {ExchangeDto.class}))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ExchangeDto>> getAllExchanges() {
        return new ResponseEntity<>(this.exchangeService.getAllExchanges(), HttpStatus.OK);
    }

    @GetMapping(value = "/exchange/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get exchange by id", description = "Returns exchange by id",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeDto.class))}),
            @ApiResponse(responseCode = "404", description = "Exchange not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ExchangeDto> readUser(@PathVariable Long id) {
        ExchangeDto exchangeDto = this.exchangeService.getExchangeById(id);
        return exchangeDto != null ? new ResponseEntity<>(exchangeDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/listing", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all listings", description = "Returns list of listings",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class,
                                    subTypes = {ListingBaseDto.class}))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ListingBaseDto>> getAllListings() {
        List<ListingBaseDto> l1 = forexService.getAllForexes().stream().map(forexMapper::forexToListingBaseDto).toList();
        return new ResponseEntity<>(l1, HttpStatus.OK);
    }

    @GetMapping(value = "/listing/get/{listingType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get specific listing based on listingType param", description = "Returns list of specific "
            + "listingType based on listingType param (forex, stock, futures)",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404", description = "Listing not found"),
            @ApiResponse(responseCode = "400", description = "listingType not valid"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getListingByType(@PathVariable String listingType) {
        if (listingType.equalsIgnoreCase("forex")) {
            return new ResponseEntity<>(forexService.getAllForexes().stream().map(forexMapper::toDto).toList(), HttpStatus.OK);
        }
        else if(listingType.equalsIgnoreCase("stock")) {
            return new ResponseEntity<>(listingStockService.getAllStocks().stream().map(stockMapper::stockDto), HttpStatus.OK);
        }
        else if(listingType.equalsIgnoreCase("futures")) {
            return new ResponseEntity<>(this.futuresService.getAllFutures().stream().map(futureMapper::toDto), HttpStatus.OK);
        } else if(listingType.equalsIgnoreCase("options")) {
            return new ResponseEntity<>(this.optionsService.getAllOptions().stream().map(optionsMapper::toDto), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/listing/history/stock/{stockId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history by stock id", description = "Returns List of histories for given stock id, "
            + "timestampFrom and timestampTo are optional (if both are provided they are inclusive, if only one is "
            + "provided it's exclusive)",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class,
                                    subTypes = {ListingHistoryDto.class}))}),
            @ApiResponse(responseCode = "404", description = "Listing not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ListingHistoryDto>> getListingsHistoryByStockId(@PathVariable Long stockId,
                                                                               @RequestParam(required = false) Integer timestampFrom,
                                                                               @RequestParam(required = false) Integer timestampTo) {

        List<ListingHistory> listingHistories = listingStockService.
                getListingHistoriesByTimestamp(stockId, timestampFrom, timestampTo);
        if (listingHistories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(listingHistories.stream().map(listingHistoryMapper::toDto).toList(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/listing/history/forex/{forexId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history by forex id", description = "Returns List of histories for given forex id, "
            + "timestampFrom and timestampTo are optional (if both are provided they are inclusive, if only one is "
            + "provided it's exclusive)",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class,
                                    subTypes = {ListingHistoryDto.class}))}),
            @ApiResponse(responseCode = "404", description = "Listing not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ListingHistoryDto>> getListingsHistoryByForexId(@PathVariable Long forexId,
                                                                               @RequestParam(required = false) Integer timestampFrom,
                                                                               @RequestParam(required = false) Integer timestampTo) {

        List<ListingHistory> listingHistories = forexService.
                getListingHistoriesByTimestamp(forexId, timestampFrom, timestampTo);
        if (listingHistories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(listingHistories.stream().map(listingHistoryMapper::toDto).toList(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/listing/history/future/{futureId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history by future id", description = "Returns List of histories for given future id, "
            + "timestampFrom and timestampTo are optional (if both are provided they are inclusive, if only one is "
            + "provided it's exclusive)",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class,
                                    subTypes = {ListingHistoryDto.class}))}),
            @ApiResponse(responseCode = "404", description = "Listing not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ListingHistoryDto>> getListingsHistoryByFutureId(@PathVariable Long futureId,
                                                                               @RequestParam(required = false) Integer timestampFrom,
                                                                               @RequestParam(required = false) Integer timestampTo) {

        List<ListingHistory> listingHistories = futuresService.
                getListingHistoriesByTimestamp(futureId, timestampFrom, timestampTo);
        if (listingHistories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(listingHistories.stream().map(listingHistoryMapper::toDto).toList(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/listing/stock/{stockId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get stock by id", description = "Returns stock by id",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingStockDto.class))}),
            @ApiResponse(responseCode = "404", description = "Stock not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ListingStockDto> getStockById(@PathVariable Long stockId) {
        ListingStock stock = listingStockService.findById(stockId).orElse(null);
        if (stock == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ListingStockDto listingStockDto = stockMapper.stockDto(stock);
        return new ResponseEntity<>(listingStockDto, HttpStatus.OK);
    }

    @GetMapping(value = "/exchange/stock/{stockId}/time", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get working time of exchange center", description = "Returns working time of exchange by stock id",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Stock working time not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getStockWorkingTime(@PathVariable Long stockId) {
        String workingTime = listingStockService.getWorkingTimeById(stockId);

        if (workingTime == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //TEMPORARY
        return new ResponseEntity<>("OPENED", HttpStatus.OK);
//        return new ResponseEntity<>(workingTime, HttpStatus.OK);
    }

    @GetMapping(value = "/listing/forex/{forexId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get forex by id", description = "Returns forex by id",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingForexDto.class))}),
            @ApiResponse(responseCode = "404", description = "Forex not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ListingForexDto> getForexById(@PathVariable Long forexId) {
        ListingForex forex = forexService.findById(forexId).orElse(null);
        if (forex == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ListingForexDto listingForexDto = forexMapper.toDto(forex);
        return new ResponseEntity<>(listingForexDto, HttpStatus.OK);
    }

    @GetMapping(value = "/listing/future/{futureId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get future by id", description = "Returns future by id",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingForexDto.class))}),
            @ApiResponse(responseCode = "404", description = "Future not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ListingFutureDto> getFutureById(@PathVariable Long futureId) {
        ListingFuture future = futuresService.findById(futureId).orElse(null);
        if (future == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ListingFutureDto listingFutureDto = futureMapper.toDto(future);
        return new ResponseEntity<>(listingFutureDto, HttpStatus.OK);
    }


    ///// OPTIONS /////

    @GetMapping(value = "/listing/options/{optionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get option by id", description = "Returns option by id",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingOptionsDto.class))}),
            @ApiResponse(responseCode = "404", description = "Option not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ListingOptionsDto> getOptionsById(@PathVariable Long optionId) {
        OptionsModel option = optionsService.findById(optionId).orElse(null);
        if (option == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ListingOptionsDto listingOptionsDto = optionsMapper.toDto(option);
        return new ResponseEntity<>(listingOptionsDto, HttpStatus.OK);
    }

    @GetMapping(value = "/listing/options", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get options", description = "Returns options",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingOptionsDto.class))}),
            @ApiResponse(responseCode = "404", description = "Option not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ListingOptionsDto>> getOptions() {
        List<OptionsModel> option = optionsService.getAllOptions();
        if (option == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Use Stream to map ListingOptions to OptionsDto and collect to a list
        List<ListingOptionsDto> listingOptionsDtoList = option.stream()
                .map(optionsMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(listingOptionsDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/listing/options/{expirationDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get option by expiration date", description = "Returns option by expiration date",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingOptionsDto.class))}),
            @ApiResponse(responseCode = "404", description = "Option not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ListingOptionsDto>> getOptionsByExpirationDate(@PathVariable Long expirationDate) {
        List<OptionsModel> options = this.optionsService.findByExpirationDate(expirationDate).orElse(Collections.emptyList());
        if (options == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ListingOptionsDto> listingOptionsDto = options.stream()
                .map(optionsMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(listingOptionsDto, HttpStatus.OK);
    }
}
