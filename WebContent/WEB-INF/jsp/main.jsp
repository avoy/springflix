<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

  	<title>SpringFlix - Main</title>

    <!-- Colorbox -->
    <!--  <link media="screen" rel="stylesheet" href="/colorbox.css" /> 6/13/16 -->

    <!-- Slick -->
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/jquery.slick/1.5.9/slick.css"/>

	<!-- Add the slick-theme.css if you want default styling -->
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/jquery.slick/1.5.9/slick-theme.css"/>

    <!-- Bootstrap core CSS -->
    <!-- <link href="/dist/css/bootstrap.min.css" rel="stylesheet"> -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <!-- <link href="/assets-bs/css/ie10-viewport-bug-workaround.css" rel="stylesheet"> -->

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets-bs/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <!-- <script src="/assets-bs/js/ie-emulation-modes-warning.js"></script> 6/13/16-->

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

       <!-- Custom styles for this template -->
    <link href="carousel.css" rel="stylesheet">

    <style>

    .img-a {
         vertical-align: middle;
         width: 260px;
         box-shadow: 0 0 30px #888;
        }
        
	.slick-prev:before, .slick-next:before { 
		font-family: "Glyphicons Halflings";
		font-size: 20px; 
		line-height: 1; 
		color: GRAY; 
		opacity: 0.75; 
		}   

	.slick-prev:before { content: "\e257"; }
	.slick-next:before { content:"\e258"; }

	.slickslide {
    	height: 200px;
    	background: lightgray;
		}

	body {
   		background-color: white;
		}

	.content {
	    margin: auto;
	    padding: 0px;
	    width: 90%;
	    
		}

	.featurette-image {
		vertical-align: middle;		
		width: 450px;
		box-shadow: 0 0 30px #888;
		}

	.carousel {
		height: 500px;
		margin-bottom: 0px;
		position: relative;
		top: 0px;
		}
	
	.carousel-inner  {
		  position: relative;
		  overflow: hidden;
		  width: 75%;
		  top: 0px;
		  height: 500px;
		  margin: auto;
		}
	
	.featurette-divider {
		  margin: 50px 0; /* Space out the Bootstrap <hr> more */
		}
	
	.marketing .col-lg-4 {
		  margin-bottom: 0px;
		  text-align: center;
		}
		
	.rails {
		  margin-bottom: 0px;
		  margin: auto;
		}
	
	.rail-container {
		  height: 165px;
		  margin-left: 25px;
		  position: relative;
		}
	
	.rail-image {
		  position: absolute;
		  left: 5;
		  top: 0;
		}

	.rail-text {
		  z-index: 100;
		  position: absolute;
		  color: white;
		  font-size: 10px;
		  left: 9px;
		  top: 0px;
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
    <br></br>
    <br></br>
    <h1>SpringFlix - Spring</h1>

     <!-- Carousel
    ================================================== -->
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
    <!-- Indicators -->
      <ol class="carousel-indicators">
        <c:forEach var="asset" items="${model.masthead.children()}" varStatus="loop" >
	        <c:choose>
	            <c:when test="${loop.index=='0'}">
	        		<li data-target="#myCarousel" data-slide-to="${loop.index}" class="active"></li>
	  			</c:when>
			    <c:otherwise>
			        <li data-target="#myCarousel" data-slide-to="${loop.index}"></li>
			    </c:otherwise>
			</c:choose>
  		</c:forEach>
      </ol>
      
      <!-- Content -->
      <div class="carousel-inner" role="listbox">
           <c:forEach var="asset" items="${model.masthead.children()}" varStatus="loop">

        <c:choose>
      	    <c:when test="${loop.index=='0'}">
				<div class="item active">
  			</c:when>
	    	<c:otherwise>
	     		<div class="item">
	    	</c:otherwise>
		</c:choose>

	         <!-- IMAGE -->
	        <img class="${asset.id()}" src="${asset.primaryImage().url()}" alt="${asset.name()}">
			<div class="container">
	            <div class="carousel-caption">
	              <h1>${asset.displayName()}</h1>
	              <p>${asset.description()}</p>
	              <p><wo name=watchNaw>Watch Now</wo></p>
	            </div>
	       </div>
       
   		 </div>

        </c:forEach>
      </div> <!-- carousel inner -->


      <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
      </a>
      <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
    </div><!-- /.carousel -->

    <!-- VIDEO RAIL -->	
	<c:import url="videorail.jsp"/>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
     <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- <script>window.jQuery || document.write('<script src="/assets-bs/js/vendor/jquery.min.js"><\/script>')</script> -->
    <!-- <script src="/dist/js/bootstrap.min.js"></script> 6/13/16-->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script> 
    
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <!--  <script src="/assets-bs/js/vendor/holder.min.js"></script> 6/13/16-->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <!--  <script src="/assets-bs/js/ie10-viewport-bug-workaround.js"></script> 6/13/16-->

<!-- 
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
 -->
 
    <!--  ColorBox -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.colorbox/1.6.3/jquery.colorbox-min.js"></script>

    <!-- Slick -->
    <script type="text/javascript" src="http://cdn.jsdelivr.net/jquery.slick/1.5.9/slick.min.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        $(".watchnow").colorbox({iframe:true, innerWidth:"840px", innerHeight:"560px", closeButton:false});
    	$('.VideoRail').slick({
    	  	  centerMode: false,
			  infinite: true,
			  dots: false,
			  arrows: true,
			  slidesToShow: 5,
			  slidesToScroll: 1,
			  responsive: [
			    {
			      breakpoint: 1475,
			      settings: {
			        arrows: true,
			        slidesToShow: 4
			      }
			    },
			    {
			      breakpoint: 1175,
			      settings: {
			        arrows: true,
			        slidesToShow: 3
			      }
			    },
			    {
			      breakpoint: 900,
			      settings: {
			        arrows: true,
			        slidesToShow: 2
			      }
			    },
			    {
			      breakpoint: 600,
			      settings: {
			        arrows: true,
			        slidesToShow: 1
			      }
			    }
			  ]
    		});
    		
  	});
</script>
  </body>
</html>