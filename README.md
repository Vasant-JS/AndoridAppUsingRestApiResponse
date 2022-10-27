# AndoridAppUsingRestApiResponse
Android App with Recycler view showing CRUD operations on the list of items from REST API Endpoint Using Retrofit

PHP API:

First create a table

CREATE TABLE `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `description` text NOT NULL,
  `price` int(255) NOT NULL,
  `category_id` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `modified` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=01 DEFAULT CHARSET=utf8