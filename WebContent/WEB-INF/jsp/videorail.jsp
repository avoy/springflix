<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:forEach var="rail" items="${model.railList}" varStatus="loop">
    <!-- VIDEO RAIL -->
	<div class="content">
		<div><H3>${rail.name()}</H3></div>
		
		<!-- Rail -->
		<div class="VideoRail">
			<c:forEach var="asset" items="${rail.children()}" varStatus="loop">
				<div class="rail-container" align="center">
					<a href="details.htm?assetId=${asset.id()}"><img class="img-a rail-image" src="${asset.primaryImage().url()}" alt="${asset.name()}"></a>
					<div class="rail-text">
						<!-- <H4><wo name=displayName/></H4> -->
						<h4>${asset.displayName()}</h4>
						<!-- <p><wo name=description/></p> -->
						
					</div>
    			</div>
        	</c:forEach>
		</div>
	</div>
  </c:forEach>
	