package com.example.rbapp.user.controller.api;

import java.util.List;

public record UserRolesRequest(List<Long> userIdList) {
}
