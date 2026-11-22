package com.bancolombia.training.customers.web;
public class InvalidRequestException extends RuntimeException {
  public InvalidRequestException(String message) { super(message); }
}
