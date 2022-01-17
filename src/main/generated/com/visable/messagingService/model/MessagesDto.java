package com.visable.messagingService.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.visable.messagingService.model.MessageDto;
import com.visable.messagingService.model.PaginationDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MessagesDto
 */

public class MessagesDto  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("pagination")
  private PaginationDto pagination = null;

  @JsonProperty("messages")
  @Valid
  private List<MessageDto> messages = null;

  public MessagesDto pagination(PaginationDto pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
  */
  @ApiModelProperty(value = "")

  @Valid

  public PaginationDto getPagination() {
    return pagination;
  }

  public void setPagination(PaginationDto pagination) {
    this.pagination = pagination;
  }

  public MessagesDto messages(List<MessageDto> messages) {
    this.messages = messages;
    return this;
  }

  public MessagesDto addMessagesItem(MessageDto messagesItem) {
    if (this.messages == null) {
      this.messages = new ArrayList<>();
    }
    this.messages.add(messagesItem);
    return this;
  }

  /**
   * Get messages
   * @return messages
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<MessageDto> getMessages() {
    return messages;
  }

  public void setMessages(List<MessageDto> messages) {
    this.messages = messages;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessagesDto messages = (MessagesDto) o;
    return Objects.equals(this.pagination, messages.pagination) &&
        Objects.equals(this.messages, messages.messages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pagination, messages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MessagesDto {\n");
    
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
    sb.append("    messages: ").append(toIndentedString(messages)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

