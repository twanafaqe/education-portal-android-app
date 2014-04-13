<?php
/*
 * Servis i�in "config.inc.php" dosyasi �ok �nemli! Bu dosya servisi veritaban�na ba�lar.
* Bunun i�in veritaban� i�lemlerini kullanaca��m�z dosyalara eklememiz gerekiyor.
* "$db" nesnesini veritaban� ile ileti�im kurmam�z i�in bize sa�lar.
* T�m veritaban� sorgulamalar�n� PDO nesnesi olan "$db" ile halledebilirsiniz.
*/
require("config.inc.php");

//de�i�ken tan�mlamalar� ba�lar
$page_item = 10;					// bir sayfada g�sterilecek i�erik miktar�n� belirtiyoruz.
$query = "";				// sql sorgu c�mlesi
$query_params = array();	// sql sorgu c�mlesi i�ine yerle�tirilmi� :parametre lerin ili�ki dizisi
$response = array();		// toplanan verilerin jsona �evrilmeden �nceki dizi hali.
if (!empty($_POST['page'])) { 	// test a�amas� i�in web aray�z sunar bu user_agent olarak ta ayarlanabilir..
	$query = "
			SELECT 
			COUNT(*) AS items
			FROM paket
			"; 					// veritaban�nda ka� paket oldu�unu sorgusu
	$query_params = array();	// parametre olmad��� i�in bo� b�rak�ld�
	try {
		$stmt   = $db->prepare($query);				// sorguda s�k�nt� var m�, parametre var m� diye kontrol ediliyor.
		$result = $stmt->execute($query_params);	// parametre var ise de�erleri yerle�tirilip sorgu ger�ekle�tiriliyor
	}
	catch (PDOException $ex) { 	// sql yaz�m hatas� gibi parametre hatas� gibi hatalar� tutar
		$response["success"] = 0;
		$response["message"] = "Veritaban� Hatas� 0.";
//		$response["message"] = "Veritaban� Hatas� 0. Hata: ". $ex->getMessage();
		die(json_encode($response));
	}

	$row = $stmt->fetch();
	
	$items_count = $row['items'];
	$end_page_number = ceil( $items_count / $page_item );

	$page = isset($_POST['page']) ? (int) $_POST['page'] : 1;
	
	if($page < 1) $page = 1;
	if($page > $end_page_number) $page = $end_page_number;
	$limit = ($page - 1) * $page_item;

	$query = "
			SELECT
			P.id AS p_id,
			P.paket_adi AS p_adi,
			P.kategori AS p_kat,
			U.isim AS e_ad,
			U.soyisim AS e_soyad,
			P.resim AS p_resim
			FROM
			paket P
			LEFT
			JOIN
			users U
			ON
			P.egitmen_id = U.id
			LIMIT :limit , :item
			";

	$query_params = array(
			":limit" => $limit,
			":item" => $page_item
			);
	//execute query
	try {
		$stmt   = $db->prepare($query);
		$result = $stmt->execute($query_params);
	}
	catch (PDOException $ex) {
		$response["success"] = 0;
		$response["message"] = "Veritaban� Hatas� 1";
//		$response["message"] = "Veritaban� Hatas� 0. Hata: ". $ex->getMessage();
		die(json_encode($response));
	}

	// Finally, we can retrieve all of the found rows into an array using fetchAll
	$rows = $stmt->fetchAll();


	if ($rows) {
		$response["success"] = 1;
		$response["message"] = "Packet Listing!";
		$response["packet"]   = array();

		foreach ($rows as $row) {
			$post             = array();
			$post["packet_id"]  = $row["p_id"];
			$post["paket_adi"] = $row["p_adi"];
			$post["kategori"]    = $row["p_kat"];
			$post["egitmen"]  = $row["e_ad"]." ".$row["e_soyad"];
			$post["resim"]  = $row["p_resim"];

			 
			//update our repsonse JSON data
			array_push($response["packet"], $post);
		}

		// echoing JSON response
		$debug = isset($_POST['pretty']) ? $_POST['pretty'] : "no";
		if ($debug=="ok"){
			echo "<pre>";
			echo json_encode($response,JSON_PRETTY_PRINT|JSON_UNESCAPED_SLASHES|JSON_UNESCAPED_UNICODE);
		}else {
			echo json_encode($response);
		}

	} else {
		$response["success"] = 0;
		$response["message"] = "No Packet Available!";
		die(json_encode($response));
	}

}else {
	?>
<h1>Paket Listesi</h1>
<form action="packets.php" method="post">
	Page Number:<br /> <input type="text" name="page"
		placeholder="page number" /> <br /> <input type="checkbox"
		name="pretty" value="ok">Pretty Listing<br> <input type="submit"
		value="Listing" />
</form>
<a href="login.php">Login</a>
<br />
<a href="register.php">Register</a>

<h3>Kullan�m�</h3>
<p>
	.../packets.php dosyas�na istek olarak, <b>Post</b> metodu ile "<b>page</b>"
	g�nderilir.
</p>
<p>
	�rne�in; <b>page="1"</b>, <b>page="5"</b> gibi her sayfada 10 paket
	listelenir
</p>
<p>
	Cevap olarak <b>{"success":1,"message":"Packet
		Listing!","packet":[{"packet_id":1,"paket_adi":"Anroid","kategori":"Mobil
		Programlama","egitmen":"Mehmet
		Topcu","resim":"http:\/\/json.marifane.com\/resim\/android.png"}]}</b>
	json veriyap�s� geri d�nd�r�l�r.
</p>
<p>
	<b>"success":1</b> tam say�(int) de�eri olarak 1 ise listeleme
	ba�ar�l�, 0 ise ba�ar�s�z.
</p>
<p>
	<b>"message":"Packet Listing!"</b> string olarak ger�ekle�en olayla
	ilgili bilgi verir.
<p>
	�rne�in; <b>"Packet Listing!"</b>, <b>""No Packet Available!""</b> test
	a�amas�nda iken <b>"Database Error1. Please Try Again!"</b> gibi.
</p>
</p>
<p>
	<b>"packet":[{},{},{}]</b> json dizisi olarak paketlerin listesini
	bar�nd�r�r.
<p>
	<b>"packet_id":1</b> tam say�(int) de�eri olarak paket id bilgisini
	verir.
</p>
<p>
	<b>"paket_adi":"Anroid"</b> string olarak paketin ba�l�k bilgisini
	verir.
</p>
<p>
	<b>"kategori":"Mobil Programlama"</b> string olarak paketin kategori
	bilgisini verir.
</p>
<p>
	<b>"egitmen":"Mehmet Topcu"</b> string olarak paketi y�kleyen
	kullan�c�n�n(e�itmenin) bilgisini verir.
</p>
<p>
	<b>"resim":"http://json.marifane.com/resim/android.png"</b> string
	olarak paketin afi� resminin url bilgisini verir.
</p>
</p>
<?php
}
?>