-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-11-2025 a las 05:00:56
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gohotel`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id_cliente` int(11) NOT NULL,
  `id_plan` int(11) DEFAULT NULL,
  `Nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `id_pais` int(11) NOT NULL,
  `puntos_lealtad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id_cliente`, `id_plan`, `Nombre`, `correo`, `password`, `id_pais`, `puntos_lealtad`) VALUES
(1, 5, 'Sofia Loaiza Cabalceta', 'soloaizakbalz@gmail.com', '123456', 3, 1000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `id_empleado` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `id_rol` int(11) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`id_empleado`, `nombre`, `id_rol`, `correo`, `password`) VALUES
(1, 'Admin', 1, 'admin@gohotel.com', '123456'),
(2, 'Nixon Vargas', 1, 'nixonvv02@gmail.com', '123456'),
(3, 'Michelle Brenes', 1, 'michigb.09@hotmail.com', '123456'),
(4, 'Israel Apuy', 1, 'iapuy10014@ufide.ac.cr', '123456');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habitacion`
--

CREATE TABLE `habitacion` (
  `id_habitacion` int(11) NOT NULL,
  `id_hotel` int(11) NOT NULL,
  `numero` int(11) NOT NULL,
  `id_tipo` int(11) NOT NULL,
  `estado` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habitacion_servicio`
--

CREATE TABLE `habitacion_servicio` (
  `id_habitacion` int(11) NOT NULL,
  `id_servicio` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel`
--

CREATE TABLE `hotel` (
  `id_hotel` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `id_pais` int(11) NOT NULL,
  `ciudad` varchar(50) NOT NULL,
  `direccion` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hotel`
--

INSERT INTO `hotel` (`id_hotel`, `nombre`, `id_pais`, `ciudad`, `direccion`) VALUES
(1, 'gohotel Miami', 1, 'Miami', '1250 Collins Ave, Miami Beach, FL'),
(2, 'gohotel Toronto', 2, 'Toronto', '77 King Street West, Toronto, ON'),
(3, 'gohotel San José', 3, 'San José', 'Avenida Central, Calle 5, San José'),
(4, 'gohotel Panamá', 4, 'Ciudad de Panamá', 'Calle 50, Obarrio, Ciudad de Panamá'),
(5, 'gohotel Bogotá', 5, 'Bogotá', 'Carrera 15 # 100-21, Bogotá'),
(6, 'gohotel Cancún', 6, 'México', 'Blvd. Kukulcán Km 9, Zona Hotelera'),
(7, 'gohotel Copacabana', 7, 'Río de Janeiro', 'Av. Atlântica 2356, Copacabana'),
(8, 'gohotel Santiago', 8, 'Santiago', 'Av. Libertador Bernardo O\'Higgins 1480'),
(9, 'gohotel Buenos Aires', 9, 'Buenos Aires', 'Av. 9 de Julio 1050, CABA'),
(10, 'gohotel Guatemala', 10, 'Ciudad de Guatemala', '6A Avenida 13-55, Zona 10');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pais`
--

CREATE TABLE `pais` (
  `id_pais` int(11) NOT NULL,
  `codigo` varchar(45) NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pais`
--

INSERT INTO `pais` (`id_pais`, `codigo`, `nombre`) VALUES
(1, 'USA', 'Estados Unidos'),
(2, 'CAN', 'Canada'),
(3, 'CRI', 'Costa Rica'),
(4, 'PAN', 'Panama'),
(5, 'COL', 'Colombia'),
(6, 'MEX', 'Mexico'),
(7, 'BRA', 'Brasil'),
(8, 'CHL', 'Chile'),
(9, 'ARG', 'Argentina'),
(10, 'GTM', 'Guatemala');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `plan_lealtad`
--

CREATE TABLE `plan_lealtad` (
  `id_plan` int(11) NOT NULL,
  `nivel` varchar(45) NOT NULL,
  `descuento` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `plan_lealtad`
--

INSERT INTO `plan_lealtad` (`id_plan`, `nivel`, `descuento`) VALUES
(1, 'Bronce', 0.00),
(2, 'Plata', 5.00),
(3, 'Oro', 10.00),
(4, 'Platino', 15.00),
(5, 'Diamante', 20.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva`
--

CREATE TABLE `reserva` (
  `id_reserva` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `id_hotel` int(11) NOT NULL,
  `id_habitacion` int(11) NOT NULL,
  `fecha_entrada` datetime NOT NULL,
  `fecha_salida` datetime NOT NULL,
  `estado_reserva` varchar(45) NOT NULL,
  `creado_por` int(11) NOT NULL,
  `fecha_creacion` datetime NOT NULL,
  `modificado_por` int(11) NOT NULL,
  `fecha_modificacion` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol_empleado`
--

CREATE TABLE `rol_empleado` (
  `id_rol` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol_empleado`
--

INSERT INTO `rol_empleado` (`id_rol`, `nombre`, `estado`) VALUES
(1, 'Admin', 1),
(2, 'Recepcion', 1),
(3, 'Limpieza', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio`
--

CREATE TABLE `servicio` (
  `id_servicio` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `servicio`
--

INSERT INTO `servicio` (`id_servicio`, `nombre`, `descripcion`) VALUES
(1, 'Wi-Fi Gratis', 'Conexión a Internet incluida y de buena velocidad.'),
(2, 'Aire acondicionado', 'Control de temperatura para confort del huésped.'),
(3, 'Televisor pantalla plana', 'Entretenimiento disponible en la habitación.'),
(4, 'Smart TV / Streaming', 'Acceso a Netflix, YouTube, etc.'),
(5, 'Cama King', 'Opción muy solicitada por comodidad.'),
(6, 'Cama Queen', 'Tamaño estándar muy popular.'),
(7, 'Estacionamiento Gratis', 'Disponible en el hotel.'),
(8, 'Desayuno incluido', 'Comida disponible sin costo adicional.'),
(9, 'Caja fuerte digital', 'Para guardar objetos de valor.'),
(10, 'Jacuzzi', 'Bañera con hidromasaje.'),
(11, 'Máquina de café', 'Café de cortesía en la habitación.'),
(12, 'Mini bar', 'Bebidas y snacks disponibles.'),
(13, 'Escritorio de trabajo', 'Área para trabajar con silla ergonómica.'),
(14, 'Mini refrigeradora', 'Para guardar bebidas y alimentos.'),
(15, 'Microondas', 'Para calentar alimentos.'),
(16, 'Botellas de agua', 'Agua de cortesía para el huésped.'),
(17, 'Vista panorámica', 'Vistas especiales'),
(18, 'Terraza', 'Espacio exterior privado.'),
(19, 'Balcón', 'Espacio exterior privado.'),
(20, 'Servicio a la habitación', 'Entrega de alimentos o artículos.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_habitacion`
--

CREATE TABLE `tipo_habitacion` (
  `id_tipo` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `capacidad` tinyint(4) NOT NULL,
  `precio_base` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_habitacion`
--

INSERT INTO `tipo_habitacion` (`id_tipo`, `nombre`, `descripcion`, `capacidad`, `precio_base`) VALUES
(1, 'Habitación Double', 'Habitación con dos camas doble, Maximo 4 Personas', 4, 150.00),
(2, 'Habitación Matrimonial', 'Habitación con una cama matrimonial. Maximo 2 Personas', 2, 100.00),
(3, 'Habitación King', 'Habitación con cama King size. Maximo 2 Personas', 2, 160.00),
(4, 'Habitación Suite Junior', 'Habitación amplia con área de descanso o sala pequeña. Cama King', 2, 180.00),
(5, 'Habitación Suite', 'Habitación de lujo con sala separada y amenidades premium. Cama King', 2, 200.00),
(6, 'Habitación Suite Presidencial', 'Habitación de máximo lujo, espacios amplios y exclusivos. Cama King', 2, 250.00),
(7, 'Habitación Accesible', 'Adaptada para personas con movilidad reducida. Cama Matrimonial', 2, 100.00),
(8, 'Habitación Familiar', 'Espacio pensado para familias (2 camas + sofá cama)', 5, 110.00),
(9, 'Habitación Económica', 'Versión más accesible en costo,con dos camas doble y amenidades básicas.', 4, 80.00),
(10, 'Habitación Conectada', 'Dos habitaciones unidas por una puerta interna. Cuatro Camas Dobles', 8, 300.00);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`id_cliente`),
  ADD KEY `FK_CLIENTE_ID_PAIS_idx` (`id_pais`),
  ADD KEY `FK_CLIENTE_ID_PLAN_idx` (`id_plan`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`id_empleado`),
  ADD KEY `FK_EMPLEADO_ID_ROLE_idx` (`id_rol`);

--
-- Indices de la tabla `habitacion`
--
ALTER TABLE `habitacion`
  ADD PRIMARY KEY (`id_habitacion`),
  ADD KEY `FK_ID_HOTEL_idx` (`id_hotel`),
  ADD KEY `FK_TIPO_HABITACION` (`id_tipo`),
  ADD KEY `FK_TIPO_HABITACION_idx` (`id_tipo`);

--
-- Indices de la tabla `habitacion_servicio`
--
ALTER TABLE `habitacion_servicio`
  ADD PRIMARY KEY (`id_habitacion`,`id_servicio`),
  ADD KEY `FK_SERVICIO_idx` (`id_servicio`);

--
-- Indices de la tabla `hotel`
--
ALTER TABLE `hotel`
  ADD PRIMARY KEY (`id_hotel`),
  ADD KEY `FK_ID_PAIS_idx` (`id_pais`);

--
-- Indices de la tabla `pais`
--
ALTER TABLE `pais`
  ADD PRIMARY KEY (`id_pais`);

--
-- Indices de la tabla `plan_lealtad`
--
ALTER TABLE `plan_lealtad`
  ADD PRIMARY KEY (`id_plan`);

--
-- Indices de la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD PRIMARY KEY (`id_reserva`),
  ADD KEY `FK_RESERVA_ID_CLIENTE_idx` (`id_cliente`),
  ADD KEY `FK_RESERVA_MODIFICADO_POR` (`modificado_por`),
  ADD KEY `FK_RESERVA_CREADO_POR_idx` (`creado_por`),
  ADD KEY `FK_RESERVA_ID_HOTEL_idx` (`id_hotel`),
  ADD KEY `FK_RESERVA_ID_HABITACION_idx` (`id_habitacion`);

--
-- Indices de la tabla `rol_empleado`
--
ALTER TABLE `rol_empleado`
  ADD PRIMARY KEY (`id_rol`);

--
-- Indices de la tabla `servicio`
--
ALTER TABLE `servicio`
  ADD PRIMARY KEY (`id_servicio`);

--
-- Indices de la tabla `tipo_habitacion`
--
ALTER TABLE `tipo_habitacion`
  ADD PRIMARY KEY (`id_tipo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `reserva`
--
ALTER TABLE `reserva`
  MODIFY `id_reserva` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `FK_CLIENTE_ID_PAIS` FOREIGN KEY (`id_pais`) REFERENCES `pais` (`id_pais`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_CLIENTE_ID_PLAN` FOREIGN KEY (`id_plan`) REFERENCES `plan_lealtad` (`id_plan`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `FK_EMPLEADO_ID_ROLE` FOREIGN KEY (`id_rol`) REFERENCES `rol_empleado` (`id_rol`) ON DELETE NO ACTION;

--
-- Filtros para la tabla `habitacion`
--
ALTER TABLE `habitacion`
  ADD CONSTRAINT `FK_ID_HOTEL` FOREIGN KEY (`id_hotel`) REFERENCES `hotel` (`id_hotel`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_ID_TPO` FOREIGN KEY (`id_tipo`) REFERENCES `tipo_habitacion` (`id_tipo`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `habitacion_servicio`
--
ALTER TABLE `habitacion_servicio`
  ADD CONSTRAINT `FK_HABITACION` FOREIGN KEY (`id_habitacion`) REFERENCES `habitacion` (`id_habitacion`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_SERVICIO` FOREIGN KEY (`id_servicio`) REFERENCES `servicio` (`id_servicio`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `hotel`
--
ALTER TABLE `hotel`
  ADD CONSTRAINT `FK_HOTEL_ID_PAIS` FOREIGN KEY (`id_pais`) REFERENCES `pais` (`id_pais`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `reserva`
--
ALTER TABLE `reserva`
  ADD CONSTRAINT `FK_RESERVA_CREADO_POR` FOREIGN KEY (`creado_por`) REFERENCES `empleado` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_RESERVA_ID_CLIENTE` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_RESERVA_ID_HABITACION` FOREIGN KEY (`id_habitacion`) REFERENCES `habitacion` (`id_habitacion`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_RESERVA_ID_HOTEL` FOREIGN KEY (`id_hotel`) REFERENCES `hotel` (`id_hotel`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_RESERVA_MODIFICADO_POR` FOREIGN KEY (`modificado_por`) REFERENCES `empleado` (`id_empleado`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
