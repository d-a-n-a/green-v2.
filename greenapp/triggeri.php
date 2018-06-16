CREATE TRIGGER transfer_tranzactii_istoric
AFTER UPDATE
   ON tranzactii FOR EACH ROW

BEGIN
   DECLARE idtranzactie tranzactii.ID_TRANZACTIE%ENGINE//
   DECLARE datapredare tranzactii.data_predare%ENGINE//
   DECLARE idexpeditor tranzactii.ID_EXPEDITOR%ENGINE//
   DECLARE iddestinatar tranzactii.ID_DESTINATAR%ENGINE//
   DECLARE idanunt tranzactii.ID_ANUNT%ENGINE//
   DECLARE idstatut tranzactii.ID_STATUS%ENGINE//
   
   SELECT :old.ID_TRANZACTIE, :old.ID_EXPEDITOR, :old.ID_DESTINATAR, :old.ID_ANUNT, :old.ID_STATUS, :old.data_predare
          INTO (idtranzactie, idexpeditor, iddestinatar, idanunt, idstatus, datapredare) 
          FROM tranzactii
          WHERE :new.ID_STATUS = 7 OR :new.ID_STATUS = 6//

    INSERT INTO istoric
   ( ID_TRANZACTIE,
     ID_EXPEDITOR,
     ID_DESTINATAR, 
     ID_ANUNT,
     ID_STATUS,
     data_predare)
   VALUES
   ( idtranzactie,
     idexpeditor,
     iddestinatar,
     idanunt,
     idstatus,
     datapredare);
     
END;