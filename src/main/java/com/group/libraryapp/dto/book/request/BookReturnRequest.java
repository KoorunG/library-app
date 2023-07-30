package com.group.libraryapp.dto.book.request;

public class BookReturnRequest {

  public BookReturnRequest(String userName, String bookName) {
    this.userName = userName;
    this.bookName = bookName;
  }

  private String userName;
  private String bookName;

  public String getUserName() {
    return userName;
  }

  public String getBookName() {
    return bookName;
  }

}
