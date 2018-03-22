<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html>

<html>
  <head>
    <meta charset="UTF-8">

  	<title>OoyalaFlix - Details Page</title>

    <!-- Bootstrap core CSS -->
    <!-- <link href="/dist/css/bootstrap.min.css" rel="stylesheet"> -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">

       <!-- Custom styles for this template -->
    <link href="carousel.css" rel="stylesheet">

    <style>


	body {
   		background-color: white;
		}

	.content {
	    margin: auto;
	    padding: 0px;
	    width: 90%;
	    
		}
	.wrapper-div {
 		    margin-top: 0px;   	
    		margin-left: 80px;
			width: 700px;
		}
		

	.featurette-image {
		vertical-align: middle;		
		width: 450px;
		box-shadow: 0 0 30px #888;
		}

	
	.featurette-divider {
		  margin: 50px 0; /* Space out the Bootstrap <hr> more */
		}
	
	.marketing .col-lg-4 {
		  margin-bottom: 0px;
		  text-align: center;
		}
		
	
	</style>
	    
       <style>
    	.error-div {
 		    margin-top: 0px;   	
    		margin-left: 80px;
			width: 700px;
			height: 400px;
			line-height: 100px;
			text-align: center;
		    background-color: black;
		}
		.span-text {
		    margin-top: 170px;
			display: inline-block;
			vertical-align: middle;
			line-height: normal;
		  	color: red;
		  	font-size: 24px;
		}
		</style>
	
  </head>

  <body id='project-wrap'>
      <div class="navbar-wrapper">
      <div class="container">
        <div class="navbar navbar-inverse navbar-fixed-top large-z" role="navigation">
          <div class="container">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="https://www.ooyala.com">OoyalaFlix</a>
            </div>
            <div class="navbar-collapse collapse">
              <ul class="nav navbar-nav">
                <li class=""><a href="index.html">Home</a></li>
                <li class='active'><a href="gallery.html">Movies</a></li>
                <li><a href="sched.html">Live TV</a></li>
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Genres<b class="caret"></b></a>
                  <ul class="dropdown-menu">
                    <li><a href="gallery.html">Action</a></li>
                    <li><a href="gallery.html">Comedy</a></li>
                    <li><a href="gallery.html">Drama</a></li>
                     <li><a href="gallery.html">Documentary</a></li>
                    <li class="divider"></li>
                    <li class="dropdown-header">Television Series</li>
                    <li><a href="gallery.html">Sitcoms</a></li>
                    <li><a href="gallery.html">Reality TV</a></li>
                  </ul>
                </li>

              </ul>
              <form class="navbar-form navbar-right form-inline" role="search">
                <div class="form-group">
                  <input type="text" class="form-control" placeholder="Find Movies">
                </div>
                <button type="submit" class="btn btn-success">Search</button>
            </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <br></br>
    <br></br>

    
    
    <div class=wrapper-div>
    
    <h1>OoyalaFlix - Spring</h1>
    
  			<c:choose>
		        <c:when test="${model.isV3}">
		        	<!-- Player V3 -->
		       		Player V3 | <a href="details.htm?assetId=${model.asset.id()}">Player V4</a>
		       		
			        <script src="https://player.ooyala.com/v3/${model.asset.rendition().playerId()}"></script>
			
				    <div id="playerwrapper" style='height:480px;'></div>
				    <script>
				        OO.ready(function() {
				            window.player = OO.Player.create("playerwrapper",'${model.asset.rendition().embed_code()}', {
				            autoplay: true,
					    	"embedToken" : "${model.embedToken}"
				            });
				        });
				    </script> 
		       		
		        </c:when>
		        <c:otherwise>
		        	<!-- Player V4 -->
		        	
					<!-- Player V4 depenencies -->
					<script src="//player.ooyala.com/static/v4/stable/4.4.11/core.min.js"></script>
					<script src="//player.ooyala.com/static/v4/stable/4.4.11/video-plugin/main_html5.min.js"></script>
					<script src="//player.ooyala.com/static/v4/stable/4.4.11/video-plugin/osmf_flash.min.js"></script>
					<script src="//player.ooyala.com/static/v4/stable/4.4.11/video-plugin/bit_wrapper.min.js"></script>
					<script src="//player.ooyala.com/static/v4/stable/4.4.11/skin-plugin/html5-skin.min.js"></script>
					<link rel="stylesheet" href="//player.ooyala.com/static/v4/stable/4.4.11/skin-plugin/html5-skin.min.css"/>
		        	
		        	<a href="details.htm?assetId=${model.asset.id()}&isV3=true">Player V3</a> |  Player V4
					<div id='container'></div>
					<script>
					  var playerParam = {
					    "pcode": "${model.asset.pcode()}",
					    "playerBrandingId": "${model.asset.rendition().playerId()}",				    
					    "debug":true,
				    	"embedToken" : "${model.embedToken}",

					    "skin": {
					    	 "config": "skin.json",
					         "inline": {"shareScreen": {"embed": {"source": "<iframe width='840' height='480' frameborder='0' allowfullscreen src='//player.ooyala.com/static/v4/stable/4.4.11/skin-plugin/iframe.html?ec=${model.asset.rendition().embed_code()}&pbid=${model.asset.rendition().playerId()}&pcode=${model.asset.pcode()}'></iframe>"}}}
					    }					    
					  };
					  OO.ready(function() {
					    window.pp = OO.Player.create('container',"${model.asset.rendition().embed_code()}", playerParam);
					  });
					</script>
		        	
		        </c:otherwise>
	        </c:choose>

        <!-- Marketing messaging and featurettes
    ================================================== -->
    <!-- Wrap the rest of the page in another container to center all the content. -->
    <div class="container marketing">
      <!-- START THE FEATURETTES -->

      <hr class="featurette-divider">
      <div class="row featurette">
        <div class="col-md-5">
          <img class="featurette-image img-responsive" src="${model.asset.primaryImage().url()}"/>
        </div>
        <div class="col-md-7">
          <h2 class="featurette-heading">${model.asset.displayName()}</h2>
          <p class="lead">${model.asset.description()} - Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.
          <br>
          <br>Cast: Cast Member 1, Cast Member 2
          <br>${model.asset.rating()}
          <br>${model.asset.year()}
          </p>
        </div>
      </div>
      <hr class="featurette-divider">
      <!-- /END THE FEATURETTES -->
      <!-- FOOTER -->
      <footer>
        <p class="pull-right"><a href="#">Back to top</a></p>
        <p>&copy; 2016 Ooyala, Inc. &middot; <a href="#">Privacy</a> &middot; <a href="#">Terms</a></p>
      </footer>

    </div><!-- /.container -->
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script><!-- Latest compiled and minified JavaScript -->
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
        <!-- <script src="script.js"></script> -->
  	</div>	

  </body>
</html>