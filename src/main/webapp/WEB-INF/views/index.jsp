<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
	<!-- Force latest IE rendering engine or ChromeFrame if installed -->
	<!--[if IE]>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<![endif]-->
	<meta charset="utf-8">
	<title>图片上传平台</title>
	<meta name="description" content="File Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video for jQuery. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. Works with any server-side platform (PHP, Python, Ruby on Rails, Java, Node.js, Go etc.) that supports standard HTML form file uploads.">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- jQuery UI styles -->
	<link rel="stylesheet" href="/static/css/jquery-ui.css" id="theme">
	<!-- Demo styles -->
	<link rel="stylesheet" href="/static/css/demo.css">
	<!--[if lte IE 8]>
	<link rel="stylesheet" href="/static/css/demo-ie8.css">
	<![endif]-->
	<style>
		/* Adjust the jQuery UI widget font-size: */
		.ui-widget {
			font-size: 0.95em;
		}
	</style>
	<!-- blueimp Gallery styles -->
	<link rel="stylesheet" href="/static/css/blueimp-gallery.min.css">
	<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
	<link rel="stylesheet" href="/static/css/jquery.fileupload.css">
	<link rel="stylesheet" href="/static/css/jquery.fileupload-ui.css">
	<!-- CSS adjustments for browsers with JavaScript disabled -->
	<noscript><link rel="stylesheet" href="/static/css/jquery.fileupload-noscript.css"></noscript>
	<noscript><link rel="stylesheet" href="/static/css/jquery.fileupload-ui-noscript.css"></noscript>
</head>
<body>
<h1>图片上传平台</h1>
<!-- The file upload form used as target for the file upload widget -->
<form id="fileupload" action="/upload.do" method="POST" enctype="multipart/form-data">
	<!-- Redirect browsers with JavaScript disabled to the origin page -->
	<noscript><input type="hidden" name="redirect" value="http://localhost:9090/ulic/home.do"></noscript>
	<!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
	<div class="fileupload-buttonbar">
		<div class="fileupload-buttons">
			<!-- The fileinput-button span is used to style the file input field as button -->
            <span class="fileinput-button">
                <span>添加图片...</span>
                <input type="file" name="files[]" multiple>
            </span>
			<button type="submit" class="start">开始上传</button>
			<button type="reset" class="cancel">取消上传</button>
			<button type="button" class="delete">删除</button>
			<input type="checkbox" class="toggle">
			<!-- The global file processing state -->
			<span class="fileupload-process"></span>
		</div>
		<!-- The global progress state -->
		<div class="fileupload-progress fade" style="display:none">
			<!-- The global progress bar -->
			<div class="progress" role="progressbar" aria-valuemin="0" aria-valuemax="100"></div>
			<!-- The extended global progress state -->
			<div class="progress-extended">&nbsp;</div>
		</div>
	</div>
	<!-- The table listing the files available for upload/download -->
	<table role="presentation"><tbody class="files"></tbody></table>
</form>
<br>

<!-- The blueimp Gallery widget -->
<div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
	<div class="slides"></div>
	<h3 class="title"></h3>
	<a class="prev">‹</a>
	<a class="next">›</a>
	<a class="close">×</a>
	<a class="play-pause"></a>
	<ol class="indicator"></ol>
</div>
<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.fileName%}</p>
            <strong class="error"></strong>
        </td>
        <td>
            <p class="size">Processing...</p>
            <div class="progress"></div>
        </td>
        <td>
            {% if (!i && !o.options.autoUpload) { %}
                <button class="start" disabled>开始</button>

            {% } %}
            {% if (!i) { %}
                <button class="cancel">取消</button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>

<!-- 上传后回调显示模板 -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.pictureUrl) { %}
                    <a href="{%=file.pictureUrl%}" title="{%=file.fileName%}" download="{%=file.fileName%}" data-gallery><img src="{%=file.pictureUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                <a href="{%=file.pictureUrl%}" title="{%=file.fileName%}" download="{%=file.fileName%}" {%=file.pictureUrl?'data-gallery':''%}>{%=file.fileName%}</a>
            </p>
            {% if (file.error) { %}
                <div><span class="error">错误</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.fileSize)%}</span>
        </td>
        <td>
            <button class="delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>删除</button>
            <input type="checkbox" name="delete" value="1" class="toggle">
        </td>
    </tr>
{% } %}
</script>
<script src="/static/js/jquery-3.2.1.min.js"></script>
<script src="/static/js/jquery-ui.min.js"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="/static/js/tmpl.min.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="/static/js/load-image.all.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="/static/js/canvas-to-blob.min.js"></script>
<!-- blueimp Gallery script -->
<script src="/static/js/jquery.blueimp-gallery.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="/static/js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="/static/js/jquery.fileupload.js"></script>
<!-- The File Upload processing plugin -->
<script src="/static/js/jquery.fileupload-process.js"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="/static/js/jquery.fileupload-image.js"></script>
<!-- The File Upload audio preview plugin -->
<script src="/static/js/jquery.fileupload-audio.js"></script>
<!-- The File Upload video preview plugin -->
<script src="/static/js/jquery.fileupload-video.js"></script>
<!-- The File Upload validation plugin -->
<script src="/static/js/jquery.fileupload-validate.js"></script>
<!-- The File Upload user interface plugin -->
<script src="/static/js/jquery.fileupload-ui.js"></script>
<!-- The File Upload jQuery UI plugin -->
<script src="/static/js/jquery.fileupload-jquery-ui.js"></script>
<!-- The main application script -->
<script src="/static/js/main.js"></script>
<script>
	// Initialize the jQuery UI theme switcher:
	$('#theme-switcher').change(function () {
		var theme = $('#theme');
		theme.prop(
				'href',
				theme.prop('href').replace(
						/[\w\-]+\/jquery-ui.css/,
						$(this).val() + '/jquery-ui.css'
				)
		);
	});
</script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="js/cors/jquery.xdr-transport.js"></script>
<![endif]-->
</body>
</html>
