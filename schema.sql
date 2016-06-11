-- MySQL dump 10.13  Distrib 5.6.26, for osx10.8 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	5.6.26-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `concurso`
--

DROP TABLE IF EXISTS `concurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concurso` (
  `idconcurso` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `actualizado` datetime DEFAULT NULL,
  PRIMARY KEY (`idconcurso`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equipo`
--

DROP TABLE IF EXISTS `equipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equipo` (
  `idequipo` varchar(10) COLLATE utf8_bin NOT NULL,
  `nombre` varchar(45) COLLATE utf8_bin NOT NULL,
  `logo` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`idequipo`),
  UNIQUE KEY `id_UNIQUE` (`idequipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fecha`
--

DROP TABLE IF EXISTS `fecha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fecha` (
  `idfecha` int(11) NOT NULL,
  `idconcurso` int(11) NOT NULL,
  `nombre` varchar(45) COLLATE utf8_bin NOT NULL,
  `numero` int(11) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  PRIMARY KEY (`idfecha`),
  KEY `fk_fecha_concurso_idx` (`idconcurso`),
  CONSTRAINT `fk_fecha_concurso` FOREIGN KEY (`idconcurso`) REFERENCES `concurso` (`idconcurso`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `participacion`
--

DROP TABLE IF EXISTS `participacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participacion` (
  `idparticipacion` int(11) NOT NULL AUTO_INCREMENT,
  `idequipo` varchar(10) COLLATE utf8_bin NOT NULL,
  `idpartido` int(11) NOT NULL,
  `goles` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `local` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idparticipacion`),
  KEY `fk_partido_equipo1_idx` (`idequipo`),
  KEY `fk_participante_partido1_idx` (`idpartido`),
  CONSTRAINT `fk_participante_partido1` FOREIGN KEY (`idpartido`) REFERENCES `partido` (`idpartido`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_partido_equipo1` FOREIGN KEY (`idequipo`) REFERENCES `equipo` (`idequipo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3100 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partido`
--

DROP TABLE IF EXISTS `partido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partido` (
  `idpartido` int(11) NOT NULL AUTO_INCREMENT,
  `idfecha` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  PRIMARY KEY (`idpartido`),
  KEY `fk_partido_fecha1_idx` (`idfecha`),
  CONSTRAINT `fk_partido_fecha10` FOREIGN KEY (`idfecha`) REFERENCES `fecha` (`idfecha`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=42918 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `posicion`
--

DROP TABLE IF EXISTS `posicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posicion` (
  `idposicion` int(11) NOT NULL AUTO_INCREMENT,
  `idconcurso` int(11) DEFAULT NULL,
  `idequipo` varchar(10) COLLATE utf8_bin NOT NULL,
  `posicion` int(11) DEFAULT NULL,
  `puntos` int(11) DEFAULT NULL,
  `partidosganados` int(11) DEFAULT NULL,
  `partidosempatados` int(11) DEFAULT NULL,
  `partidosperdidos` int(11) DEFAULT NULL,
  `golesfavor` int(11) DEFAULT NULL,
  `golescontra` int(11) DEFAULT NULL,
  PRIMARY KEY (`idposicion`),
  UNIQUE KEY `ix_concurso_equipo` (`idconcurso`,`idequipo`),
  KEY `fk_posicion_equipo1_idx` (`idequipo`),
  CONSTRAINT `fk_posicion_concurso1` FOREIGN KEY (`idconcurso`) REFERENCES `concurso` (`idconcurso`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_posicion_equipo1` FOREIGN KEY (`idequipo`) REFERENCES `equipo` (`idequipo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pronostico`
--

DROP TABLE IF EXISTS `pronostico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pronostico` (
  `userid` varchar(45) NOT NULL,
  `idparticipacion` int(11) NOT NULL,
  `goles` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`userid`,`idparticipacion`),
  KEY `fk_pronostico_visitante1_idx` (`userid`),
  KEY `fk_pronostico_participacion1_idx` (`idparticipacion`),
  CONSTRAINT `fk_pronostico_participacion1` FOREIGN KEY (`idparticipacion`) REFERENCES `participacion` (`idparticipacion`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pronostico_visitante1` FOREIGN KEY (`userid`) REFERENCES `visitante` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visitante`
--

DROP TABLE IF EXISTS `visitante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitante` (
  `tipo` int(11) DEFAULT NULL,
  `userid` varchar(45) NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `token` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-06-11 12:50:32
