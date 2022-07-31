package com.example.smtpserver.pojo.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestBody {

  @Data
  @Builder
  public static class GetUserListReq {
    private long pageSize;
    private long currentPage;
    private String searchKey;
  }

  @Data
  @Builder
  public static class VerifyPasswordReq {
    private String userId;
    private String password;
  }

  @Data
  @Builder
  public static class ChangePasswordReq {
    private String userId;
    private String newPassword;
  }

  @Data
  @Builder
  public static class RegisterUserReq {
    private String account;
    private String password;
  }

  @Data
  @Builder
  public static class UpdateAuthorityReq {
    private String userId;
    private Integer authority;
  }

  @Data
  @Builder
  public static class UpdateDisabledReq {
    private String userId;
    private Integer isDisabled;
  }



}
