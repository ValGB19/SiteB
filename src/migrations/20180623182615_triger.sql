-- TRIGGER PARA Actualizar los puntos de los matchPredictions

DROP TRIGGER IF EXISTS trigMP;
DELIMITER $$
CREATE TRIGGER triMP AFTER UPDATE ON matches
FOR EACH ROW BEGIN
	IF (OLD.result <> NEW.result) THEN 
		update matches set score = 0 where match_id = NEW.id; 
        update matches set score = 3 where match_id = NEW.id and prediction = NEW.result;
	END IF;
end;$$
DELIMITER ;