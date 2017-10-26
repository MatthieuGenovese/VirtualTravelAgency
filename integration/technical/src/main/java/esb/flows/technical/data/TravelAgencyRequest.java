package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TravelAgencyRequest {

    @JsonProperty private Flight flight;
    @JsonProperty private Car car;
    @JsonProperty private Hotel hotel;

    public Flight getFlightReq() {
        return flight;
    }

    public void setFlightReq(Flight flightReq) {
        this.flight = flightReq;
    }

    public Car getCarReq() {
        return car;
    }

    public void setCarReq(Car carReq) {
        this.car = carReq;
    }

    public Hotel getHotelReq() {
        return hotel;
    }

    public void setHotelReq(Hotel hotelReq) {
        this.hotel = hotelReq;
    }

    public TravelAgencyRequest(){

    }
}
