<html>
<head>

    <title>Index</title>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <link type="text/css" href="css/index.css" rel="stylesheet"/>
    <style>
        table {
            width: 100%;
            border: 2px solid aliceblue;
        }
    </style>
</head>
<body>
<h2>Welcome to the Basic Front End page</h2>

<div id="menuBar">
    <table>
        <tr>
            <form action="booksbyauthor/">
                <table>
                    <tr>
                        <td width="33%">Filter by Author</td>
                        <td width="33%"><input type="String" name="author"></td>
                        <td width="33%"><input value="Submit" type="submit" formaction="/reader/booksbyauthor/?author="></td>
                    </tr>
                </table>
            </form>
        </tr>
        <tr>
            <form action="booksbycategory/">
                <table>
                    <tr>
                        <td width="33%">Filter by Category</td>
                        <td width="33%"><input type="String" name="category"></td>
                        <td width="33%"><input value="Submit" type="submit" formaction="/reader/booksbycategory/?category="></td>
                    </tr>
                </table>
            </form>
        </tr>
        <tr>
            <form action="booksbyavailability/">
                <table>
                    <tr>
                        <td width="33%">List books by availability</td>
                        <td> </td>
                        <td width="33%"><input value="Submit" type="submit" formaction="/reader/booksbytitle/"></td>
                    </tr>
                </table>
            </form>
        </tr>
        <tr>
            <form action="booksbytitle/">
                <table>
                    <tr>
                        <td width="33%">Filter by Title</td>
                        <td width="33%"><input type="String" name="title"></td>
                        <td width="33%"><input value="Submit" type="submit" formaction="/reader/booksbytitle/?title="></td>
                    </tr>
                </table>
            </form>
        </tr>
        <tr>
            <form action="booksbyyear/">
                <table>
                    <tr>
                        <td width="33%">Filter by Year</td>
                        <td width="33%"><input type="year" name="year"></td>
                        <td width="33%"><input value="Submit" type="submit" formaction="/reader/booksbyyear/?year="></td>
                    </tr>
                </table>
            </form>
        </tr>
        <tr>
            <form action="borrowings/">
                <table>
                    <tr>
                        <td width="33%">Filter by Borrowings</td>
                        <td width="33%"><input type="String" name="borrowings"></td>
                        <td width="33%"><input value="Submit" type="submit" formaction="/reader/borrowings/?username="></td>
                    </tr>
                </table>
            </form>
        </tr>
        <tr>
            <form action="allbook/">
                <table>
                    <tr>
                        <td width="33%">List all Books</td>
                        <td></td>
                        <td width="33%"><input value="Submit" type="submit" formaction="/reader/allbook/"></td>
                    </tr>
                </table>
            </form>
        </tr>

    </table>
</div>




</body>
</html>