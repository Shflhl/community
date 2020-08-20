package life.community.dto;

import java.util.ArrayList;
import java.util.List;

public class PageDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious = true;

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    private boolean showFirstPage;
    private boolean showNext = true;
    private boolean showEndPage;
    private Integer page;

    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public boolean isShowFirstPage() {
        return showFirstPage;
    }

    public void setShowFirstPage(boolean showFirstPage) {
        this.showFirstPage = showFirstPage;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowEndPage() {
        return showEndPage;
    }

    public void setShowEndPage(boolean showEndPage) {
        this.showEndPage = showEndPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPage(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;

        int preNum = page - 3;
        int nextNum = page + 3;
        if (preNum > 1)
            showFirstPage = true;
        if (nextNum < totalPage)
            showEndPage = true;
        for (int i = page - 1;(i > 0)&&(i >= preNum);i--)
            pages.add(0,i);
        pages.add(page);
        for (int j = page + 1;(j <= totalPage)&&(j <= nextNum);j++)
            pages.add(j);

        if (page == 1)
            showPrevious = false;
        if (page == totalPage)
            showNext = false;
    }
}
