-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 09, 2018 at 04:29 PM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.2

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `greenapp`
--

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `modificare_status_anunt`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `modificare_status_anunt` ()  NO SQL
BEGIN
  DECLARE anunt INT;
  DECLARE status_anunt INT;
  DECLARE data_anunt DATE;
 
  
  DECLARE done TINYINT(1);
  DECLARE cursor_anunturi CURSOR FOR SELECT ID_ANUNT, data_introducerii, ID_STATUS FROM anunturi;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cursor_anunturi; 

  read_loop: LOOP
    FETCH cursor_anunturi INTO anunt, data_anunt, status_anunt;
    IF done THEN
      LEAVE read_loop;
    END IF;
    IF DATEDIFF(SYSDATE(), data_anunt) >= 1 AND status_anunt = 1  THEN
      update anunturi set ID_STATUS = 3 WHERE ID_ANUNT = anunt;
    END IF;
  END LOOP;

  CLOSE cursor_anunturi; 
END$$

DROP PROCEDURE IF EXISTS `reminder_intalnire`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `reminder_intalnire` ()  NO SQL
BEGIN
  DECLARE id_tranz INT;
  DECLARE status_tranzactie INT;
  DECLARE data_tranzactie DATE;
  DECLARE expeditor INT;
  DECLARE destinatar INT;
  DECLARE anunt INT;
  DECLARE produs varchar(100);
  DECLARE username_expeditor varchar(100);
  DECLARE username_destinatar varchar(100);
  DECLARE ora varchar(50);
  
  DECLARE done TINYINT(1);
  DECLARE cursor_tranzactii CURSOR FOR SELECT ID_TRANZACTIE, ID_STATUS, data_predare, ora_predare, ID_EXPEDITOR, ID_DESTINATAR, ID_ANUNT FROM tranzactii;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cursor_tranzactii; 

  read_loop: LOOP
    FETCH cursor_tranzactii INTO id_tranz, status_tranzactie, data_tranzactie, ora, expeditor, destinatar, anunt;
    IF done THEN
      LEAVE read_loop;
    END IF;
    IF DATEDIFF(SYSDATE(), data_tranzactie) = 0 AND status_tranzactie = 5  THEN
    	SELECT username INTO username_expeditor from users where ID_USER = expeditor;
        SELECT username INTO username_destinatar from users where ID_USER = destinatar;
        SELECT p.denumire INTO produs from produse p, anunturi a where a.ID_PRODUS = p.ID_PRODUS 
        and a.ID_ANUNT = anunt;
    
    
    insert INTO notificari(tip,data,descriere) VALUES
    ("sistem",  SYSDATE(), CONCAT('Azi, la ora ', ora, ' ai o intalnire cu ', username_destinatar, ' pentru       produsul ', produs,'. ' ));
    INSERT INTO notificari_useri(ID_NOTIFICARE, ID_USER,citit)
    VALUES (@@IDENTITY,expeditor,FALSE); 
    
    insert INTO notificari(tip,data,descriere) VALUES
    ("sistem",  SYSDATE(), CONCAT('Azi, la ora ', ora, ' ai o intalnire cu ', username_expeditor, ' pentru       produsul ', produs,'. ' ));
    INSERT INTO notificari_useri(ID_NOTIFICARE, ID_USER,citit)
    VALUES (@@IDENTITY,destinatar,FALSE); 
      
    END IF;
  END LOOP;

  CLOSE cursor_tranzactii; 
END$$

DROP PROCEDURE IF EXISTS `top_utilizatori`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `top_utilizatori` ()  NO SQL
BEGIN
 DECLARE id INT;
 DECLARE nr INT;
 DECLARE nume VARCHAR(255);
 DECLARE done INT;
 DECLARE cursor_utilizatori cursor for select ID_EXPEDITOR, count(*) from istoric where ID_STATUS = 7 GROUP by ID_EXPEDITOR LIMIT 3;
  
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 OPEN cursor_utilizatori;

  read_loop: LOOP
    FETCH cursor_utilizatori INTO id, nr;
     IF done THEN
        LEAVE read_loop;
    END IF;
       SELECT u.username, l.strada, l.oras, (select avg(nota) from reviews where ID_USER = id) as nota_generala, nr from users u, locatii l where u.ID_USER = id and u.ID_LOCATIE = l.ID_LOCATIE;
  END LOOP;

  CLOSE cursor_utilizatori;
END$$

DROP PROCEDURE IF EXISTS `transfer_tranzactii_istoric`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `transfer_tranzactii_istoric` ()  BEGIN
  DECLARE id_tranz INT;
  DECLARE status_tranzactie INT;
  DECLARE data_tranzactie DATE;
  DECLARE expeditor INT;
  DECLARE destinatar INT;
  DECLARE anunt INT;
  
  DECLARE done TINYINT(1);
  DECLARE cursor_tranzactii CURSOR FOR SELECT ID_TRANZACTIE, ID_STATUS, data_predare, ID_EXPEDITOR, ID_DESTINATAR, ID_ANUNT FROM tranzactii;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cursor_tranzactii; 

  read_loop: LOOP
    FETCH cursor_tranzactii INTO id_tranz, status_tranzactie, data_tranzactie, expeditor, destinatar, anunt;
    IF done THEN
      LEAVE read_loop;
    END IF;
    IF DATEDIFF(SYSDATE(), data_tranzactie) >=1 AND (status_tranzactie = 7 OR status_tranzactie = 6)  THEN
      INSERT INTO istoric(ID_TRANZACTIE, ID_EXPEDITOR, ID_DESTINATAR, ID_ANUNT, ID_STATUS, data_predare)
      			  VALUES (id_tranz,expeditor,destinatar,anunt,status_tranzactie,data_tranzactie); 
                  SET foreign_key_checks = 0;
      DELETE FROM tranzactii where ID_TRANZACTIE = id_tranz;
      SET foreign_key_checks = 1;
    END IF;
  END LOOP;

  CLOSE cursor_tranzactii; 
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `anunturi`
--

DROP TABLE IF EXISTS `anunturi`;
CREATE TABLE `anunturi` (
  `ID_ANUNT` int(10) NOT NULL,
  `data_introducerii` date NOT NULL,
  `durata_in_zile` int(10) NOT NULL,
  `tip` varchar(10) NOT NULL,
  `descriere` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `ID_USER` int(10) NOT NULL,
  `ID_PRODUS` int(10) DEFAULT NULL,
  `ID_STATUS` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `anunturi`:
--   `ID_STATUS`
--       `statusuri` -> `ID_STATUS`
--   `ID_USER`
--       `users` -> `ID_USER`
--   `ID_PRODUS`
--       `produse` -> `ID_PRODUS`
--

--
-- Dumping data for table `anunturi`
--

INSERT INTO `anunturi` (`ID_ANUNT`, `data_introducerii`, `durata_in_zile`, `tip`, `descriere`, `ID_USER`, `ID_PRODUS`, `ID_STATUS`) VALUES
(12, '2018-06-21', 15, 'oferta', 'Ojele din imagine. Neutilizate.', 12, 136, 3),
(13, '2018-06-22', 15, 'oferta', 'Tricou gri cu broderie manuala, marimea M. Nepurtat.', 12, 137, 3),
(15, '2018-06-16', 15, 'oferta', 'Rosii proaspete din gradina.', 17, 139, 3),
(16, '2018-06-15', 18, 'oferta', 'Visine culese din gradina.', 17, 140, 3),
(17, '2018-06-15', 14, 'oferta', 'Vin rosu, 3 sticle nedesfacute.', 15, 141, 4),
(19, '2018-06-15', 15, 'oferta', 'Gem de prune facut in casa in decembrie 2017', 16, 142, 3),
(20, '2018-06-16', 15, 'oferta', '2kg kiwi in stare foarte buna.', 13, 143, 3),
(24, '2018-06-15', 3, 'cerere', 'Pantaloni de stofa pentru barbat. Marimea 46.', 18, 135, 3),
(26, '2018-06-22', 20, 'oferta', 'Compotul este realizat in casa cu visine din gradina. Nu are zahar adaugat.', 15, 134, 4),
(71, '2018-06-23', 14, 'oferta', 'Gem facut in casa dupa reteta veche. Nu contine zahar.', 16, 176, 3),
(84, '2018-06-24', 20, 'cerere', 'Mure proaspete. Cantitate mare.', 12, 180, 3),
(86, '2018-06-25', 20, 'cerere', 'Orice tip de haine pentru copiii unei familii sarace. Sunt 3 baieti de 8, 10 si 12 ani.', 12, 182, 1),
(87, '2018-06-23', 10, 'oferta', 'Carti pentru copii de gradinita. Sunt in jur de 30 in total.', 12, 183, 4);

-- --------------------------------------------------------

--
-- Table structure for table `categorii`
--

DROP TABLE IF EXISTS `categorii`;
CREATE TABLE `categorii` (
  `ID_CATEGORIE` int(10) NOT NULL,
  `denumire` varchar(255) NOT NULL,
  `descriere` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `categorii`:
--

--
-- Dumping data for table `categorii`
--

INSERT INTO `categorii` (`ID_CATEGORIE`, `denumire`, `descriere`) VALUES
(1, 'haine', 'Produse de vestimentatie. Produse textile. Incaltaminte.'),
(3, 'alimente', 'Produse alimentare generale. Bauturi, fructe, legume, preparate.'),
(8, 'altele', 'Orice alt tip de produs, care nu se incadreaza nici la haine, nici la alimente.');

-- --------------------------------------------------------

--
-- Table structure for table `istoric`
--

DROP TABLE IF EXISTS `istoric`;
CREATE TABLE `istoric` (
  `ID` int(10) NOT NULL,
  `ID_TRANZACTIE` int(10) NOT NULL,
  `ID_EXPEDITOR` int(10) NOT NULL,
  `ID_DESTINATAR` int(10) NOT NULL,
  `ID_ANUNT` int(10) NOT NULL,
  `ID_STATUS` int(10) NOT NULL,
  `data_predare` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `istoric`:
--   `ID_TRANZACTIE`
--       `tranzactii` -> `ID_TRANZACTIE`
--

--
-- Dumping data for table `istoric`
--

INSERT INTO `istoric` (`ID`, `ID_TRANZACTIE`, `ID_EXPEDITOR`, `ID_DESTINATAR`, `ID_ANUNT`, `ID_STATUS`, `data_predare`) VALUES
(2, 1, 15, 12, 17, 7, '2018-04-30'),
(3, 2, 12, 17, 12, 7, '2018-04-30'),
(4, 4, 12, 16, 12, 7, '2018-04-30'),
(5, 5, 12, 8, 13, 6, '2018-04-30'),
(6, 6, 12, 15, 13, 7, '2018-04-30'),
(7, 9, 12, 8, 12, 7, '2018-06-07'),
(8, 10, 12, 17, 12, 6, '2018-06-08'),
(9, 12, 12, 8, 87, 7, '2018-06-10');

-- --------------------------------------------------------

--
-- Table structure for table `locatii`
--

DROP TABLE IF EXISTS `locatii`;
CREATE TABLE `locatii` (
  `ID_LOCATIE` int(10) NOT NULL,
  `tara` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `oras` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `strada` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `latitudine` decimal(20,10) NOT NULL,
  `longitudine` decimal(20,10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `locatii`:
--

--
-- Dumping data for table `locatii`
--

INSERT INTO `locatii` (`ID_LOCATIE`, `tara`, `oras`, `strada`, `latitudine`, `longitudine`) VALUES
(2, 'Romania', 'Bucuresti', 'Strada Mihai Eminescu, nr. 9', '44.4470899000', '26.1096052000'),
(3, 'Romania', 'Bucuresti', 'PiaÈ›a Victoriei, nr. 23', '44.4527347000', '26.0853554000'),
(4, 'Romania', 'Bucuresti', 'Bulevardul LascÄƒr Catargiu, nr. 125', '44.4492952000', '26.0919365000'),
(5, 'Romania', 'Bucuresti', 'Str. Imparatul Traian, nr. 9', '44.3925100000', '26.1085270000'),
(6, 'Romania', 'Bucuresti', 'Piata Victoriei, nr. 331', '44.3213976000', '25.9099083000');

-- --------------------------------------------------------

--
-- Table structure for table `notificari`
--

DROP TABLE IF EXISTS `notificari`;
CREATE TABLE `notificari` (
  `ID_NOTIFICARE` int(10) NOT NULL,
  `descriere` varchar(255) NOT NULL,
  `data` date NOT NULL,
  `tip` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `notificari`:
--

--
-- Dumping data for table `notificari`
--

INSERT INTO `notificari` (`ID_NOTIFICARE`, `descriere`, `data`, `tip`) VALUES
(1, 'Au fost adaugate 5 anunturi.', '2018-05-03', 'sistem'),
(2, 'Au fost adaugate 1 anunt.', '2018-05-03', 'sistem'),
(3, 'Au fost adaugate 3 anunturi.', '2018-05-03', 'sistem'),
(4, 'Utilizatorul alina v-a evaluat cu nota 5 pentru ultima tranzactie.', '2018-06-02', 'USER'),
(5, 'Utilizatorul dana v-a evaluat cu nota 5 pentru ultima tranzactie.', '2018-06-02', 'USER'),
(7, 'Utilizatorul maria v-a evaluat cu nota 5 pentru ultima tranzactie efectuata.', '2018-06-10', 'USER'),
(8, 'Utilizatorul maria v-a evaluat cu nota 5 pentru ultima tranzactie efectuata.', '2018-06-10', 'USER'),
(9, 'Utilizatorul maria v-a evaluat cu nota 5 pentru ultima tranzactie efectuata.', '2018-06-10', 'USER'),
(10, 'Utilizatorul alina v-a evaluat cu nota 4 pentru ultima tranzactie efectuata.', '2018-06-10', 'USER'),
(11, 'Utilizatorul iulian v-a evaluat cu nota 5 pentru ultima tranzactie efectuata.', '2018-06-16', 'USER'),
(12, 'Utilizatorul alina v-a evaluat cu nota 4 pentru ultima tranzactie efectuata.', '2018-06-16', 'USER'),
(13, 'Utilizatorul alina v-a evaluat cu nota 3 pentru ultima tranzactie efectuata.', '2018-06-22', 'USER'),
(15, 'Azi, la ora 18:30 ai o intalnire cu alina pentru       produsul Compot de visine. ', '2018-06-25', 'sistem'),
(16, 'Azi, la ora 18:30 ai o intalnire cu iulian pentru       produsul Compot de visine. ', '2018-06-25', 'sistem');

-- --------------------------------------------------------

--
-- Table structure for table `notificari_useri`
--

DROP TABLE IF EXISTS `notificari_useri`;
CREATE TABLE `notificari_useri` (
  `ID_NOTIFICARE` int(10) NOT NULL,
  `ID_USER` int(10) NOT NULL,
  `citit` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `notificari_useri`:
--   `ID_USER`
--       `users` -> `ID_USER`
--   `ID_NOTIFICARE`
--       `notificari` -> `ID_NOTIFICARE`
--

--
-- Dumping data for table `notificari_useri`
--

INSERT INTO `notificari_useri` (`ID_NOTIFICARE`, `ID_USER`, `citit`) VALUES
(1, 8, 0),
(1, 16, 0),
(2, 8, 0),
(3, 8, 0),
(3, 16, 0),
(4, 8, 1),
(5, 12, 0),
(7, 15, 0),
(8, 15, 0),
(9, 15, 0),
(10, 8, 0),
(11, 12, 1),
(12, 15, 0),
(13, 16, 0),
(15, 15, 0),
(16, 12, 0);

-- --------------------------------------------------------

--
-- Table structure for table `produse`
--

DROP TABLE IF EXISTS `produse`;
CREATE TABLE `produse` (
  `ID_PRODUS` int(10) NOT NULL,
  `denumire` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `valabilitate` date NOT NULL,
  `imagine` varchar(255) NOT NULL,
  `detalii` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `ID_CATEGORIE` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `produse`:
--   `ID_CATEGORIE`
--       `categorii` -> `ID_CATEGORIE`
--

--
-- Dumping data for table `produse`
--

INSERT INTO `produse` (`ID_PRODUS`, `denumire`, `valabilitate`, `imagine`, `detalii`, `ID_CATEGORIE`) VALUES
(134, 'Compot de visine', '2018-09-30', '/greenapp/images/product-upload134.png', 'Punctul de intalnire si ora pot fi stabilite prin mesaje.', 3),
(135, 'Pantaloni', '0000-00-00', '/greenapp/images/wanted.png', 'Detaliile intalnirii le stabilim impreuna.', 1),
(136, 'Oja', '2018-06-01', '/greenapp/images/product-upload136.png', 'Disponibil dupa ora 18:00 in fiecare zi.', 8),
(137, 'Tricou', '2018-06-02', '/greenapp/images/product-upload137.png', 'Intalnirea se stabileste prin mail.', 1),
(139, 'Rosii', '2018-04-30', '/greenapp/images/product-upload139.png', 'Disponibilitate dupa ora 19:00 in fiecare zi.', 3),
(140, 'Visine', '2018-06-30', '/greenapp/images/product-upload140.png', 'Orice zi, orice ora pentru intalnire.', 3),
(141, 'Vin', '2018-12-31', '/greenapp/images/product-upload141.png', 'Disponibilitate dimineata, pana in ora 12:00.', 3),
(142, 'Gem de prune', '2018-08-31', '/greenapp/images/product-upload142.png', 'Se stabilesc detalile pe mail.', 3),
(143, 'Kiwi', '2018-06-05', '/greenapp/images/product-upload143.png', 'Ã®ntÃ¢lnire dupÃ£ ora 19:30. De preferat, la locatia indicatÃ£.', 3),
(176, 'Gem de nuci', '2018-12-31', '/greenapp/images/product-upload176.png', 'Punctul de intalnire si ora le stabilim impreuna prin email.', 3),
(180, 'Mure', '0000-00-00', '/greenapp/images/wanted.png', 'Intalnirea oriunde in Bucuresti. In orice zi.', 3),
(181, 'Banane', '2018-06-16', '/greenapp/images/product-upload181.png', 'de sters detaliibananr', 3),
(182, 'Haine de primavara', '0000-00-00', '/greenapp/images/wanted.png', 'Oricand, in orice zi. Voi prelua personal de la locatia indicata.', 1),
(183, 'Carti', '2018-10-31', '/greenapp/images/product-upload183.png', 'Le pot preda in oricare zi, dupa ora 19:00.', 8);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews` (
  `ID_REVIEW` int(10) NOT NULL,
  `ID_AUTOR` int(10) NOT NULL,
  `ID_USER` int(10) NOT NULL,
  `data_postarii` date NOT NULL,
  `detalii` varchar(255) CHARACTER SET utf8 NOT NULL,
  `nota` float NOT NULL,
  `ID_TRANZACTIE` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `reviews`:
--   `ID_AUTOR`
--       `users` -> `ID_USER`
--   `ID_USER`
--       `users` -> `ID_USER`
--   `ID_TRANZACTIE`
--       `tranzactii` -> `ID_TRANZACTIE`
--

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`ID_REVIEW`, `ID_AUTOR`, `ID_USER`, `data_postarii`, `detalii`, `nota`, `ID_TRANZACTIE`) VALUES
(1, 15, 12, '2018-04-02', 'Intalnire si predare realizate fara probleme.', 5, 1),
(2, 12, 15, '2018-04-02', 'Tranzatie efectuata fara probleme.', 4, NULL),
(3, 12, 16, '2018-04-02', 'Maria nu a respectat ora stabilita. Produsul a fost exact ca cel prezentat.', 2, NULL),
(9, 15, 12, '2018-04-02', 'A fost in regula toata experienta.', 4, 6),
(10, 16, 12, '2018-04-02', 'Totul a decurs ok.', 5, 4),
(11, 12, 15, '2018-04-02', 'Totul a decurs in regula', 4, 1),
(15, 12, 8, '2018-06-02', 'A fost ok predarea. ', 4, 6),
(17, 8, 12, '2018-06-02', 'Perfect. Exact produsul la care ma asteptam. Fara probleme.', 5, 6),
(18, 15, 12, '2018-06-16', 'Totul a decurs cum trebuie.', 5, 13),
(19, 12, 15, '2018-06-16', 'Au fost respectate detaliile stabilite.', 4, 13),
(20, 12, 16, '2018-06-22', 'Procesul a decurs asa cum am stabilit.', 5, 14);

--
-- Triggers `reviews`
--
DROP TRIGGER IF EXISTS `after_review_insert_notification`;
DELIMITER $$
CREATE TRIGGER `after_review_insert_notification` AFTER INSERT ON `reviews` FOR EACH ROW begin
 
    declare mesaj varchar(500);
    declare username_autor varchar(255);
	declare nota_evaluare float;

    SELECT username INTO username_autor FROM users where ID_USER = new.ID_AUTOR;
    SELECT nota INTO nota_evaluare FROM reviews where ID_REVIEW = new.ID_REVIEW;
    
    insert INTO notificari(tip,data,descriere) VALUES
    ("USER",  SYSDATE(), CONCAT('Utilizatorul ',CONVERT(`username_autor` using utf8),' v-a evaluat cu nota ', nota_evaluare, ' pentru ultima tranzactie efectuata.'));
    
    INSERT INTO notificari_useri(ID_NOTIFICARE, ID_USER,citit)
    VALUES (@@IDENTITY,new.ID_USER,FALSE); 
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `statusuri`
--

DROP TABLE IF EXISTS `statusuri`;
CREATE TABLE `statusuri` (
  `ID_STATUS` int(10) NOT NULL,
  `tip` varchar(255) NOT NULL,
  `descriere` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `statusuri`:
--

--
-- Dumping data for table `statusuri`
--

INSERT INTO `statusuri` (`ID_STATUS`, `tip`, `descriere`) VALUES
(1, 'nou', 'Anuntul este valabil si adaugat recent.'),
(3, 'disponibil', 'Produsul corespuzator anuntului este inca disponibil.'),
(4, 'indisponibil', 'Produsul corespunzator anuntului nu mai este disponibil'),
(5, 'in curs', 'Tranzactia a fost inregistrata si este in curs de a fi efectuata.'),
(6, 'anulat', 'Unul dintre utilizatori a anulat intalnirea.'),
(7, 'finalizat', 'S-a facut predarea bunului.'),
(8, 'rezervat', 'Produsul a fost solicitat de un utilizator. Ramane ca cei doi sa se intalneasca pentru a se face predarea.');

-- --------------------------------------------------------

--
-- Table structure for table `tranzactii`
--

DROP TABLE IF EXISTS `tranzactii`;
CREATE TABLE `tranzactii` (
  `ID_TRANZACTIE` int(10) NOT NULL,
  `locatie_predare` varchar(255) NOT NULL,
  `data_predare` date NOT NULL,
  `ora_predare` varchar(10) NOT NULL,
  `ID_EXPEDITOR` int(10) NOT NULL,
  `ID_DESTINATAR` int(10) NOT NULL,
  `ID_ANUNT` int(10) NOT NULL,
  `ID_STATUS` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `tranzactii`:
--   `ID_STATUS`
--       `statusuri` -> `ID_STATUS`
--   `ID_EXPEDITOR`
--       `users` -> `ID_USER`
--   `ID_DESTINATAR`
--       `users` -> `ID_USER`
--   `ID_ANUNT`
--       `anunturi` -> `ID_ANUNT`
--

--
-- Dumping data for table `tranzactii`
--

INSERT INTO `tranzactii` (`ID_TRANZACTIE`, `locatie_predare`, `data_predare`, `ora_predare`, `ID_EXPEDITOR`, `ID_DESTINATAR`, `ID_ANUNT`, `ID_STATUS`) VALUES
(13, 'Str. Arthur Verona, nr. 14', '2018-06-25', '18:30', 15, 12, 26, 7),
(14, 'Strada Rosetti, nr. 12', '2018-06-24', '19:45', 12, 16, 13, 7);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `ID_USER` int(10) NOT NULL,
  `nume` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `prenume` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `parola` varchar(255) NOT NULL,
  `telefon` varchar(14) NOT NULL,
  `data_inregistrarii` varchar(255) NOT NULL,
  `fotografie` varchar(255) NOT NULL,
  `biografie` varchar(255) CHARACTER SET utf8 COLLATE utf8_romanian_ci NOT NULL,
  `ID_LOCATIE` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `users`:
--   `ID_LOCATIE`
--       `locatii` -> `ID_LOCATIE`
--

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID_USER`, `nume`, `prenume`, `email`, `username`, `parola`, `telefon`, `data_inregistrarii`, `fotografie`, `biografie`, `ID_LOCATIE`) VALUES
(8, 'Neagu', 'Dana', 'dana.neagu@gmail.com', 'dana', '9e57deb7bd397124abbe1adb2cbfd035', '0737683007', '2-April-2017', '/greenapp/images/user.png', 'Alandana. Space. Welcome!', 5),
(11, 'Cristina', 'Neagu', 'cristina@gmail.com', 'cristina', '9e57deb7bd397124abbe1adb2cbfd035', '0720538311', '02-April-2018', '/greenapp/images/user.png', 'cristina space. welcome. buy. sell.', 5),
(12, 'Cristescu', 'Alina', 'alina@gmail.com', 'alina', '9e57deb7bd397124abbe1adb2cbfd035', '0720526487', '2-April-2018', '/greenapp/images/user.png', 'Bine ai venit in contul meu online!', 3),
(13, 'Marinescu', 'Iolanda', 'iolo@gmail.com', 'iolanda', '613397a0126cdfed747dfedc6c9e1a3e', '0721458766', '1-April-2018', '/greenapp/images/user.png', 'Iolanda landa landa. Wlcm!', 4),
(15, 'Iulian', 'Ioan', 'iulian@gmail.com', 'iulian', '9e57deb7bd397124abbe1adb2cbfd035', '0725436981', '18-April-2018', '/greenapp/images/user.png', 'Acesta este spatiul meu. Voi adauga doar anunturi care contin alimente.', 3),
(16, 'Maria', 'Popa', 'maria@gmail.com', 'maria', '9e57deb7bd397124abbe1adb2cbfd035', '0724587524', '18-April-2018', '/greenapp/images/user.png', 'Buna! Pe pagina mea vei gasi anunturi cu produse alimentare.', 3),
(17, 'Lavinia', 'Giurgea', 'lavinia@gmail.com', 'lavinia', '9e57deb7bd397124abbe1adb2cbfd035', '0735469872', '18-April-2018', '/greenapp/images/user.png', '', 3),
(18, 'Dragos', 'Damian', 'dragos@gmail.com', 'dragos', '9e57deb7bd397124abbe1adb2cbfd035', '0723548678', '18-April-2018', '/greenapp/images/user.png', '', 3),
(22, 'Matilda', 'Ionescu', 'matilda@gmail.com', 'matilda', '9e57deb7bd397124abbe1adb2cbfd035', '0737683214', '09-June-2018', '/greenapp/images/user.png', 'Bine te-am gasit! Produsele pe care le postez sunt de obicei fructe si legume, asa ca se dua repede.', 5),
(23, 'Eliza', 'Olteanca', 'elisa@gmail.com', 'eliza', '9e57deb7bd397124abbe1adb2cbfd035', '0737685421', '07-July-2018', '', 'Produse alimentare! Contul meu online!', 6);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `anunturi`
--
ALTER TABLE `anunturi`
  ADD PRIMARY KEY (`ID_ANUNT`),
  ADD UNIQUE KEY `ID_PRODUS` (`ID_PRODUS`),
  ADD KEY `FK_anunturi_statusuri` (`ID_STATUS`),
  ADD KEY `ID_STATUS` (`ID_USER`);

--
-- Indexes for table `categorii`
--
ALTER TABLE `categorii`
  ADD PRIMARY KEY (`ID_CATEGORIE`),
  ADD KEY `ID_CATEGORIE` (`ID_CATEGORIE`);

--
-- Indexes for table `istoric`
--
ALTER TABLE `istoric`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_TRANZACTIE` (`ID_TRANZACTIE`);

--
-- Indexes for table `locatii`
--
ALTER TABLE `locatii`
  ADD PRIMARY KEY (`ID_LOCATIE`);

--
-- Indexes for table `notificari`
--
ALTER TABLE `notificari`
  ADD PRIMARY KEY (`ID_NOTIFICARE`),
  ADD KEY `index_notificare` (`ID_NOTIFICARE`);

--
-- Indexes for table `notificari_useri`
--
ALTER TABLE `notificari_useri`
  ADD PRIMARY KEY (`ID_NOTIFICARE`,`ID_USER`),
  ADD KEY `ID_USER` (`ID_USER`);

--
-- Indexes for table `produse`
--
ALTER TABLE `produse`
  ADD PRIMARY KEY (`ID_PRODUS`),
  ADD KEY `ID_CATEGORIE` (`ID_CATEGORIE`),
  ADD KEY `ID_ANUNT` (`ID_PRODUS`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`ID_REVIEW`),
  ADD KEY `index_autor` (`ID_AUTOR`),
  ADD KEY `index_user` (`ID_USER`),
  ADD KEY `index_tranzactie` (`ID_TRANZACTIE`);

--
-- Indexes for table `statusuri`
--
ALTER TABLE `statusuri`
  ADD PRIMARY KEY (`ID_STATUS`),
  ADD KEY `FK_statusuri_anunturi` (`ID_STATUS`);

--
-- Indexes for table `tranzactii`
--
ALTER TABLE `tranzactii`
  ADD PRIMARY KEY (`ID_TRANZACTIE`),
  ADD KEY `index_status` (`ID_STATUS`),
  ADD KEY `index_anunt` (`ID_ANUNT`),
  ADD KEY `index_expeditor` (`ID_EXPEDITOR`),
  ADD KEY `index_destinatar` (`ID_DESTINATAR`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID_USER`),
  ADD KEY `index_locatie` (`ID_LOCATIE`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `anunturi`
--
ALTER TABLE `anunturi`
  MODIFY `ID_ANUNT` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=88;

--
-- AUTO_INCREMENT for table `categorii`
--
ALTER TABLE `categorii`
  MODIFY `ID_CATEGORIE` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `istoric`
--
ALTER TABLE `istoric`
  MODIFY `ID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `locatii`
--
ALTER TABLE `locatii`
  MODIFY `ID_LOCATIE` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `notificari`
--
ALTER TABLE `notificari`
  MODIFY `ID_NOTIFICARE` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `produse`
--
ALTER TABLE `produse`
  MODIFY `ID_PRODUS` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=184;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `ID_REVIEW` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `statusuri`
--
ALTER TABLE `statusuri`
  MODIFY `ID_STATUS` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `tranzactii`
--
ALTER TABLE `tranzactii`
  MODIFY `ID_TRANZACTIE` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID_USER` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `anunturi`
--
ALTER TABLE `anunturi`
  ADD CONSTRAINT `FK_anunturi_statusuri` FOREIGN KEY (`ID_STATUS`) REFERENCES `statusuri` (`ID_STATUS`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `anunturi_ibfk_1` FOREIGN KEY (`ID_USER`) REFERENCES `users` (`ID_USER`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `anunturi_ibfk_2` FOREIGN KEY (`ID_PRODUS`) REFERENCES `produse` (`ID_PRODUS`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `notificari_useri`
--
ALTER TABLE `notificari_useri`
  ADD CONSTRAINT `notificari_useri_ibfk_3` FOREIGN KEY (`ID_USER`) REFERENCES `users` (`ID_USER`) ON UPDATE CASCADE,
  ADD CONSTRAINT `notificari_useri_ibfk_4` FOREIGN KEY (`ID_NOTIFICARE`) REFERENCES `notificari` (`ID_NOTIFICARE`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `produse`
--
ALTER TABLE `produse`
  ADD CONSTRAINT `produse_ibfk_1` FOREIGN KEY (`ID_CATEGORIE`) REFERENCES `categorii` (`ID_CATEGORIE`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`ID_AUTOR`) REFERENCES `users` (`ID_USER`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`ID_USER`) REFERENCES `users` (`ID_USER`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reviews_ibfk_3` FOREIGN KEY (`ID_TRANZACTIE`) REFERENCES `tranzactii` (`ID_TRANZACTIE`) ON UPDATE NO ACTION;

--
-- Constraints for table `tranzactii`
--
ALTER TABLE `tranzactii`
  ADD CONSTRAINT `tranzactii_ibfk_1` FOREIGN KEY (`ID_STATUS`) REFERENCES `statusuri` (`ID_STATUS`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tranzactii_ibfk_2` FOREIGN KEY (`ID_EXPEDITOR`) REFERENCES `users` (`ID_USER`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tranzactii_ibfk_3` FOREIGN KEY (`ID_DESTINATAR`) REFERENCES `users` (`ID_USER`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tranzactii_ibfk_4` FOREIGN KEY (`ID_ANUNT`) REFERENCES `anunturi` (`ID_ANUNT`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`ID_LOCATIE`) REFERENCES `locatii` (`ID_LOCATIE`) ON DELETE NO ACTION ON UPDATE NO ACTION;

DELIMITER $$
--
-- Events
--
DROP EVENT `eliminare_anunturi`$$
CREATE DEFINER=`root`@`localhost` EVENT `eliminare_anunturi` ON SCHEDULE EVERY 1 DAY STARTS '2018-04-29 16:48:00' ON COMPLETION NOT PRESERVE DISABLE COMMENT 'Stergere anunturi iesite din termenul stabilit.' DO BEGIN
        DELETE a, p FROM greenapp.anunturi a, produse p WHERE a.ID_PRODUS = P.ID_PRODUS AND (DATEDIFF(sysdate(), a.data_introducerii) > a.durata_in_zile OR sysdate() > p.valabilitate) AND (SELECT count(*) FROM tranzactii t WHERE t.ID_ANUNT = a.ID_ANUNT) = 0;
   END$$

DROP EVENT `tranzactii_istoric`$$
CREATE DEFINER=`root`@`localhost` EVENT `tranzactii_istoric` ON SCHEDULE EVERY 1 DAY STARTS '2018-06-10 18:21:00' ON COMPLETION NOT PRESERVE DISABLE COMMENT 'Tranzactiile finalizate/anulate trec dupa o zi in istoric' DO BEGIN
	call transfer_tranzactii_istoric();
END$$

DROP EVENT `adaugare_notificari`$$
CREATE DEFINER=`root`@`localhost` EVENT `adaugare_notificari` ON SCHEDULE EVERY 1 DAY STARTS '2018-06-25 06:00:00' ON COMPLETION NOT PRESERVE ENABLE COMMENT 'Introducere notificare pentru reminder intalnire' DO BEGIN
	CALL reminder_intalnire();
END$$

DROP EVENT `status_anunt`$$
CREATE DEFINER=`root`@`localhost` EVENT `status_anunt` ON SCHEDULE EVERY 1 DAY STARTS '2018-06-25 23:59:59' ON COMPLETION NOT PRESERVE ENABLE DO begin 
	call modificare_status_anunt();
end$$

DELIMITER ;
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
