package com.example.rbapp.bitrix.client;

import com.example.rbapp.bitrix.api.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StubBitrixClient implements BitrixClient {

    @Override
    public CreateClientResponse createClient(CreateClientRequest createClientRequest) {
        log.info("Create client");
        return new CreateClientResponse(1L);
    }

    @Override
    public CreateClientResponse createContactWithType(CreateContactWithTypeRequest createContactWithTypeRequest) {
        return null;
    }

    @Override
    public BitrixResponse createUser(CreateUserRequest createUserRequest) {
        log.info("Create user");
        return new BitrixResponse(0L);
    }

    @Override
    public void createInvitation(InvitationRequest invitationRequest) {
        log.info("Create invitation");
    }

    @Override
    public void createClientApplication(CreateClientApplicationRequest clientApplicationRequest) {
        log.info("Create client application");
    }

    @Override
    public void createDeal(CreateDealRequest createDealRequest) {
        log.info("Create deal");
    }

    @Override
    public void createPaymentCard(CreatePaymentCardRequest createPaymentCardRequest) {
        log.info("create payment");
    }

    @Override
    public ClientListResponse getClientList(ClientListRequest clientListRequest) {
        return new ClientListResponse(0L);
    }

    @Override
    public void createBookPurchaseApplication(CreateBookPurchaseApplicationRequest bookPurchaseApplicationRequest) {
        log.info("Create book purchase application");
    }
}
