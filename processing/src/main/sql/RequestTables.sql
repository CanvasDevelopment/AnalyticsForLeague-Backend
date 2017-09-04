Create TABLE RiotApiRequests(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  requestTime LONG
);

INSERT INTO RiotApiRequests(requestTime) VALUES (1234545678);

select * from RiotApiRequests where id > 0 ORDER BY id DESC ;

select count(*) as numberOfRequests from RiotApiRequests