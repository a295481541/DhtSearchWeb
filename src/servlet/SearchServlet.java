package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pojo.PageInfo;

import com.konka.dhtsearch.db.luncene.LuceneSearchResult;
import com.konka.dhtsearch.db.luncene.LuceneUtils;
import com.konka.dhtsearch.util.ArrayUtils;
import com.konka.dhtsearch.util.TextUtils;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 5355659034287728426L;
	public static final String PAGE = "page", SEARCHKEYWORDS = "searchkeywords";
	public static final String TOTAL = "total";
	public static final String ANDROID="android";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		String page = request.getParameter(PAGE);// 页码
		String searchkeywords = request.getParameter(SEARCHKEYWORDS);// 关键字
		int currentPage = TextUtils.isEmpty(page) ? 1 : Integer.parseInt(page);
		System.out.println(searchkeywords);
		LuceneSearchResult luceneSearchResult = null;
		int total = 0;
		try {
			luceneSearchResult = LuceneUtils.search(searchkeywords, currentPage);
			total = luceneSearchResult.getTotal();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (!ArrayUtils.isEmpty(luceneSearchResult.getLists())) {
			int pagecount = Math.round(total / 10f);
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()//
					+ path + request.getServletPath();
			PageInfo pageInfo = new PageInfo(currentPage, pagecount, basePath, request);
			pageInfo.addParam(SEARCHKEYWORDS, null);
			pageInfo.generate();
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("luceneSearchResult", luceneSearchResult);

			request.getRequestDispatcher("/SearchResult.jsp").forward(request, response);

		} else {
			// PrintWriter out = response.getWriter();
			// out.println("没有找到");
			// out.flush();
			// out.close();
			request.getRequestDispatcher("/404.jsp").forward(request, response);
		}

	}
public static void main(String[] args) {
	  try {
		LuceneUtils.search("dd", 1);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	@Override
	public void init() throws ServletException {
		super.init();
	}
}
