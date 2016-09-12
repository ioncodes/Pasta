<html lang="de">
    <link rel="stylesheet" href="css/dracula.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/perfect-scrollbar.min.css">

    <?php
        require_once "constants.php";

        $id = $_REQUEST["paste"];

        $url = "pastes/".$id.FILE_EXTENSION;

        $content = file_get_contents($url);

        echo '<pre><code>'.str_replace(array("<", ">"), array("&lt;", "&gt;"), $content).'</code></pre>';
    ?>
    <!--<script type="application/javascript" src="js/jquery-3.1.0.min.js" ></script>-->
    <script type="application/javascript" src="js/highlight.pack.js" ></script>
    <script type="application/javascript" src="js/perfect-scrollbar.min.js" ></script>
    <script>
        hljs.initHighlightingOnLoad();
        var el = document.querySelector('pre');
        Ps.initialize(el);
    </script>
</html>