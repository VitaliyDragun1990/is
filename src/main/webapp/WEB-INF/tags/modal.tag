<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ attribute name="id" required="true" type="java.lang.String" %>
<%@ attribute name="message" required="true" type="java.lang.String" %>
<%@ attribute name="title" required="false" type="java.lang.String" %>

<div id="${id}" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button class="close" type="button" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">${title != null ? title : 'Modal title'}</h4>
			</div>
			<div class="modal-body">
				<p>${message}</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default cancel" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary save">Save changes</button>
			</div>
		</div>
	</div>
</div>