package com.example.rbapp.bitrix.client;

import com.example.rbapp.bitrix.api.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("bitrix")
public interface BitrixClient {

    @PostMapping("/crm.contact.add")
    CreateClientResponse createClient(@RequestBody CreateClientRequest createClientRequest);

    @PostMapping("/user.add")
    BitrixResponse createUser(@RequestBody CreateUserRequest createUserRequest);

    @PostMapping("")
    void createInvitation(@RequestBody InvitationRequest invitationRequest);

    @PostMapping("/crm.contact.add")
    void createClientApplication(@RequestBody CreateClientApplicationRequest clientApplicationRequest);

    @PostMapping("/crm.deal.add")
    void createDeal(@RequestBody CreateDealRequest createDealRequest);

    @PostMapping("/crm.item.add")
    void createPaymentCard(@RequestBody CreatePaymentCardRequest createPaymentCardRequest);

    @PostMapping("/crm.contact.list")
    ClientListResponse getClientList(@RequestBody ClientListRequest clientListRequest);
}
