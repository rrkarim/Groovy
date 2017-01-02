-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 02, 2017 at 08:04 AM
-- Server version: 10.1.16-MariaDB
-- PHP Version: 5.5.38

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `barama`
--

-- --------------------------------------------------------

--
-- Table structure for table `followers`
--

CREATE TABLE `followers` (
  `id` int(6) NOT NULL,
  `uid` int(5) NOT NULL,
  `upid` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `followers`
--

INSERT INTO `followers` (`id`, `uid`, `upid`) VALUES
(1, 1, 7),
(2, 1, 8),
(3, 8, 1),
(4, 1, 10);

-- --------------------------------------------------------

--
-- Table structure for table `genres`
--

CREATE TABLE `genres` (
  `id` int(5) NOT NULL,
  `name` varchar(30) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `genres`
--

INSERT INTO `genres` (`id`, `name`) VALUES
(1, 'Hip Hop'),
(2, 'Rock'),
(3, 'R''n''B'),
(4, 'Classical'),
(5, 'Jazz');

-- --------------------------------------------------------

--
-- Table structure for table `likes`
--

CREATE TABLE `likes` (
  `id` int(6) NOT NULL,
  `uid` int(5) NOT NULL,
  `pid` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `likes`
--

INSERT INTO `likes` (`id`, `uid`, `pid`) VALUES
(77, 1, 21),
(78, 8, 15),
(79, 8, 19),
(80, 8, 1),
(81, 1, 19),
(82, 1, 15),
(83, 1, 21),
(84, 1, 21),
(85, 1, 12),
(86, 1, 20),
(87, 1, 1),
(88, 8, 14),
(89, 8, 13),
(90, 8, 10),
(91, 8, 3),
(92, 1, 8);

-- --------------------------------------------------------

--
-- Table structure for table `news`
--

CREATE TABLE `news` (
  `id` int(6) NOT NULL,
  `title` varchar(60) COLLATE utf8_bin NOT NULL,
  `singer` varchar(30) COLLATE utf8_bin NOT NULL,
  `header_image` varchar(60) COLLATE utf8_bin NOT NULL,
  `text` text COLLATE utf8_bin NOT NULL,
  `type_id` int(10) NOT NULL,
  `likes_count` int(5) NOT NULL DEFAULT '0',
  `rep_counts` int(5) NOT NULL,
  `author_id` int(5) NOT NULL,
  `album` varchar(40) COLLATE utf8_bin NOT NULL,
  `date` datetime NOT NULL,
  `track` varchar(50) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `news`
--

INSERT INTO `news` (`id`, `title`, `singer`, `header_image`, `text`, `type_id`, `likes_count`, `rep_counts`, `author_id`, `album`, `date`, `track`) VALUES
(1, 'Bitch, Don''t Kill My Vibe', 'Kendrick Lamar', 'http://10.0.2.2/images/10.jpg', 'Kendrick Lamar', 1, 66, 1, 1, '', '2016-12-15 00:00:00', 'http://10.0.2.2/songs/ken1.mp3'),
(2, 'Smells Like Teen Spirit', 'Nirvana', 'http://10.0.2.2/images/6.jpg', 'sdssdsd', 2, 57, 0, 7, '', '2016-12-14 04:30:18', 'http://10.0.2.2/songs/nirv1.mp3'),
(3, 'Wake Me Up When September', 'Green Day', 'http://10.0.2.2/images/7.jpg', 'sdfdsfs', 2, 41, 0, 8, '', '2016-12-15 00:00:00', 'http://10.0.2.2/songs/green1.mp3'),
(8, 'i (Explicit)', 'Kendrick Lamar', 'http://10.0.2.2/images/55.jpg', '', 1, 10, 1, 1, '', '2016-12-16 00:00:00', 'http://10.0.2.2/songs/ken2.mp3'),
(10, 'M.A.A.D. City', 'Kendrick Lamar', 'http://10.0.2.2/images/11.jpg', '', 0, 11, 0, 8, '', '2016-12-16 23:00:00', 'http://10.0.2.2/songs/ken3.mp3'),
(12, 'Everybody Dies', 'J. Cole', 'http://10.0.2.2/images/12.jpg', '', 0, 6, 1, 1, '', '2016-12-17 00:00:00', 'http://10.0.2.2/songs/cole1.mp3'),
(13, 'Oblivion', 'Grimes', 'http://10.0.2.2/images/13.jpg', '', 128, 4, 0, 8, '', '2016-12-17 00:00:00', 'http://10.0.2.2/songs/grimes1.mp3'),
(14, 'False Prophets', 'J. Cole', 'http://10.0.2.2/images/14.jpg', '', 1, 17, 0, 8, '', '2016-12-17 10:00:00', 'http://10.0.2.2/songs/cole2.mp3'),
(15, 'Hearing Damage', 'Thom Yorke', 'http://10.0.2.2/images/5015986.jpg', '', 2, 13, 1, 10, '', '2016-12-23 00:00:00', 'http://10.0.2.2/songs/thom.mp3'),
(16, 'High & Dry', 'Thom Yorke', 'http://10.0.2.2/images/radiohead-the-best-of-432129.jpg', '', 2, 123, 23, 10, '', '2016-12-14 00:00:00', 'http://10.0.2.2/songs/thom.mp3'),
(19, 'What''''s the Story Morning Glory?', 'Oasis', 'http://10.0.2.2/images/album_04.jpg', '', 9, 5, 2, 8, '', '2016-12-23 00:00:00', 'http://10.0.2.2/songs/ken2.mp3'),
(20, 'Rattle And Hum', 'U2', 'http://10.0.2.2/images/600.jpg', '', 9, 13, 1, 7, '', '2016-12-23 00:00:00', 'http://10.0.2.2/songs/ken3.mp3'),
(21, 'Everybody Hurts', 'R.E.M.', 'http://10.0.2.2/images/1336582.jpg', '', 9, 196, 24, 1, '', '2016-12-23 00:00:00', 'http://10.0.2.2/songs/rem.mp3');

-- --------------------------------------------------------

--
-- Table structure for table `repost`
--

CREATE TABLE `repost` (
  `id` int(6) NOT NULL,
  `uid` int(5) NOT NULL,
  `pid` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `repost`
--

INSERT INTO `repost` (`id`, `uid`, `pid`) VALUES
(1, 1, 15),
(2, 1, 19),
(3, 1, 20),
(4, 1, 8),
(5, 1, 21),
(6, 1, 1),
(7, 8, 19),
(8, 1, 12);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(20) NOT NULL,
  `username` varchar(50) COLLATE utf8_bin NOT NULL,
  `password` varchar(50) COLLATE utf8_bin NOT NULL,
  `email` varchar(50) COLLATE utf8_bin NOT NULL,
  `name` varchar(20) COLLATE utf8_bin NOT NULL,
  `surname` varchar(20) COLLATE utf8_bin NOT NULL,
  `image` varchar(50) COLLATE utf8_bin NOT NULL,
  `image_small` varchar(50) COLLATE utf8_bin NOT NULL,
  `date` date NOT NULL,
  `fol_count` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`, `name`, `surname`, `image`, `image_small`, `date`, `fol_count`) VALUES
(1, 'root@mail.ru', '63a9f0ea7bb98050796b649e85481845', 'root@mail.ru', 'Rasul', 'Kerimov', 'http://10.0.2.2/user_images/11855380.jpg', 'http://10.0.2.2/user_images/11855380_small.jpg', '2016-12-14', 0),
(7, 'memmedhacilli', '63a9f0ea7bb98050796b649e85481845', 'sample@', 'Memmed', 'Hacili', 'http://10.0.2.2/user_images/memmed.jpg', 'http://10.0.2.2/user_images/memmed_small.jpg', '2016-12-14', 0),
(8, 'stoya', '63a9f0ea7bb98050796b649e85481845', 'st@gmail.com', 'Ayots', 'Atfd', 'http://10.0.2.2/user_images/17-Stoya.jpg', 'http://10.0.2.2/user_images/17-Stoya.jpg', '2016-12-14', 5),
(10, 'thom', '63a9f0ea7bb98050796b649e85481845', 'thom@gmail.com', 'Thom', 'Yorke', 'http://10.0.2.2/user_images/thom.jpg', 'http://10.0.2.2/user_images/thom.jpg', '2016-12-23', 1029);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `followers`
--
ALTER TABLE `followers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `genres`
--
ALTER TABLE `genres`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `news`
--
ALTER TABLE `news`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `repost`
--
ALTER TABLE `repost`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `followers`
--
ALTER TABLE `followers`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `genres`
--
ALTER TABLE `genres`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `likes`
--
ALTER TABLE `likes`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=93;
--
-- AUTO_INCREMENT for table `news`
--
ALTER TABLE `news`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- AUTO_INCREMENT for table `repost`
--
ALTER TABLE `repost`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
