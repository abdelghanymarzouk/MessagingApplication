package com.visable.messagingService.model;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Message 
 */
@ApiModel(description = "Message ")

public class MessageDto  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("content")
  private String content;

  @JsonProperty("sentBy")
  private UUID sentBy;

  @JsonProperty("sentTo")
  private UUID sentTo;

  @JsonProperty("sentOn")
  private LocalDateTime sentOn;

  public MessageDto content(String content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
  */
  @ApiModelProperty(value = "")


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public MessageDto sentBy(UUID sentBy) {
    this.sentBy = sentBy;
    return this;
  }

  /**
   * Get sentBy
   * @return sentBy
  */
  @ApiModelProperty(readOnly = true, value = "")

  @Valid

  public UUID getSentBy() {
    return sentBy;
  }

  public void setSentBy(UUID sentBy) {
    this.sentBy = sentBy;
  }

  public MessageDto sentTo(UUID sentTo) {
    this.sentTo = sentTo;
    return this;
  }

  /**
   * Get sentTo
   * @return sentTo
  */
  @ApiModelProperty(value = "")

  @Valid

  public UUID getSentTo() {
    return sentTo;
  }

  public void setSentTo(UUID sentTo) {
    this.sentTo = sentTo;
  }

  public MessageDto sentOn(LocalDateTime sentOn) {
    this.sentOn = sentOn;
    return this;
  }

  /**
   * Get sentOn
   * @return sentOn
  */
  @ApiModelProperty(readOnly = true, value = "")

  @Valid

  public LocalDateTime getSentOn() {
    return sentOn;
  }

  public void setSentOn(LocalDateTime sentOn) {
    this.sentOn = sentOn;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessageDto message = (MessageDto) o;
    return Objects.equals(this.content, message.content) &&
        Objects.equals(this.sentBy, message.sentBy) &&
        Objects.equals(this.sentTo, message.sentTo) &&
        Objects.equals(this.sentOn, message.sentOn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, sentBy, sentTo, sentOn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MessageDto {\n");
    
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    sentBy: ").append(toIndentedString(sentBy)).append("\n");
    sb.append("    sentTo: ").append(toIndentedString(sentTo)).append("\n");
    sb.append("    sentOn: ").append(toIndentedString(sentOn)).append("\n");
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

