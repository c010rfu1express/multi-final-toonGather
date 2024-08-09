package com.multi.toonGather.social.controller;

import com.multi.toonGather.common.exception.AccessDeniedException;
import com.multi.toonGather.common.model.dto.PageDTO;
import com.multi.toonGather.security.CustomUserDetails;
import com.multi.toonGather.social.model.dto.ActivityDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryCommentDTO;
import com.multi.toonGather.social.model.dto.diary.DiaryDTO;
import com.multi.toonGather.social.model.dto.review.ReviewDTO;
import com.multi.toonGather.social.service.SocialService;
import com.multi.toonGather.user.model.dto.UserDTO;
import com.multi.toonGather.webtoon.model.dto.WebtoonDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 소셜 기능 관련 요청을 처리하는 컨트롤러
 *
 * @author : seoyun
 * @fileName : SocialController
 * @since : 2024-07-24
 */
@Slf4j
@Controller
@RequestMapping("/social")
public class SocialController {

    private final SocialService socialService;

    /**
     * Instantiates a new Social controller.
     *
     * @param socialService the social service
     */
    @Autowired
    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    /**
     * Main string.
     * 툰투게더 메인 페이지를 처리하는 컨트롤러 메서드
     *
     * @param model the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/main")
    public String main(Model model) throws Exception {
        // 인기 리뷰 상위 10개 조회
        List<ReviewDTO> popularReviews = socialService.getPopularReviews(10);
        // 조회한 인기 리뷰를 모델에 추가
        model.addAttribute("popularReviews", popularReviews);
        // 메인 페이지 뷰 반환
        return "social/main";
    }

    /**
     * Search response entity.
     * 툰투게더 메인 페이지의 검색 기능을 처리하는 API 엔드포인트
     *
     * @param category 검색 카테고리 (리뷰, 다이어리, 사용자)
     * @param keyword  검색어
     * @param page     페이지 번호 (기본값: 0)
     * @param size     한 페이지당 결과 수 (기본값: 6)
     * @return ResponseEntity<?>  검색 결과와 추가 데이터 로딩 가능 여부를 포함한 응답
     */
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestParam("category") String category,
                                    @RequestParam("keyword") String keyword,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "6") int size) {
        try {
            // 검색 서비스 호출
            List<?> searchResults = socialService.search(category, keyword, page, size);

            // 추가 데이터 로딩 가능 여부 확인
            boolean hasMore = searchResults.size() == size + 1;
            if (hasMore) {
                // 추가 데이터 확인을 위해 가져온 마지막 항목 제거
                searchResults.remove(searchResults.size() - 1);
            }

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("results", searchResults);
            response.put("hasMore", hasMore);

            // 성공 응답 반환
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 오류 발생 시 에러 메시지와 함께 500 상태 코드 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred during the search. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    /**
     * User profile string.
     * 사용자 프로필 페이지를 처리하는 컨트롤러 메서드
     *
     * @param userId      조회할 사용자의 ID
     * @param currentUser 현재 로그인한 사용자 정보
     * @param model       the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/users/{userId}/profile")
    public String userProfile(@PathVariable("userId") String userId,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              Model model) throws Exception {
        // 프로필 사용자 정보 조회
        UserDTO profileUser = socialService.selectUserProfile(userId);
        int followingCount = socialService.getFollowingCount(userId);
        int followerCount = socialService.getFollowerCount(userId);

        // 사용자의 최애 웹툰 목록 조회
        List<ReviewDTO> favoriteWebtoons = socialService.getFavoriteWebtoons(userId);

        // 사용자의 인기 리뷰 조회: 12개
        List<ReviewDTO> popularReviews = socialService.getPopularReviewsByUser(userId, 12);

        // 사용자가 팔로우하는 사용자 목록 조회
        List<UserDTO> followingUsers = socialService.getFollowingUsers(profileUser.getUserNo());

        // 최근 활동 가져오기 및 필터링: 5개
        List<ActivityDTO> recentActivities = socialService.getRecentActivities(userId, 5);
        recentActivities = recentActivities.stream()
                .filter(activity -> {
                    switch (activity.getActivityType()) {
                        case "REVIEW_LIKE":
                        case "REVIEW_CREATE":
                            return activity.getReview() != null;
                        case "DIARY_COMMENT":
                        case "DIARY_CREATE":
                            return activity.getDiary() != null;
                        default:
                            return false;
                    }
                })
                .limit(5)
                .collect(Collectors.toList());

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("favoriteWebtoons", favoriteWebtoons);
        model.addAttribute("popularReviews", popularReviews);
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("recentActivities", recentActivities);

        boolean isOwnProfile = false;
        boolean isFollowing = false;

        // 현재 로그인한 사용자 정보 처리
        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);

            // 자신의 프로필인지 확인
            isOwnProfile = currentUserDTO.getUserId().equals(userId);

            // 다른 사용자의 프로필일 경우 팔로우 여부 확인
            if (!isOwnProfile) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
            }
        }

        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("isFollowing", isFollowing);

        return "social/user/profile";
    }

    /**
     * User follows string.
     * 팔로우/팔로잉 목록 페이지를 처리하는 컨트롤러 메서드
     *
     * @param userId 조회할 사용자의 ID
     * @param type   the type
     * @param model  the model
     * @return the string
     */
    @GetMapping("/users/{userId}/follows")
    public String userFollows(@PathVariable("userId") String userId,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              @RequestParam(name = "type", required = false, defaultValue = "following") String type,
                              Model model) throws Exception {

        // 프로필 사용자 정보 조회
        UserDTO profileUser = socialService.selectUserProfile(userId);
        int followingCount = socialService.getFollowingCount(userId);
        int followerCount = socialService.getFollowerCount(userId);

        List<UserDTO> follows;
        if ("followers".equals(type)) {
            follows = socialService.getFollowers(userId);
        } else {
            follows = socialService.getFollowing(userId);
        }

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("follows", follows);
        model.addAttribute("type", type);
        model.addAttribute("userId", userId);

        boolean isOwnProfile = false;
        boolean isFollowing = false;

        // 현재 로그인한 사용자 정보 처리
        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);

            // 자신의 프로필인지 확인
            isOwnProfile = currentUserDTO.getUserId().equals(userId);

            // 다른 사용자의 프로필일 경우 팔로우 여부 확인
            if (!isOwnProfile) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
            }
        }

        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("isFollowing", isFollowing);

        return "social/user/follows";
    }

    /**
     * Toggle follow response entity.
     * 팔로우/언팔로우 기능을 처리하는 컨트롤러 메서드
     *
     * @param userId      팔로우/언팔로우 대상 사용자의 ID
     * @param currentUser 현재 로그인한 사용자 정보
     * @return ResponseEntity<?>  팔로우/언팔로우 결과와 현재 팔로우 상태를 포함한 응답
     */
    @PostMapping("/users/{userId}/follow")
    @ResponseBody
    public ResponseEntity<?> toggleFollow(@PathVariable("userId") String userId,
                                          @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            // 대상 사용자 정보 조회
            UserDTO targetUser = socialService.selectUserProfile(userId);

            // 팔로우/언팔로우 토글 수행 및 결과 확인
            boolean isNowFollowing = socialService.toggleFollow(currentUser.getUserDTO().getUserNo(), targetUser.getUserNo());

            // 성공 응답 반환 (팔로우 상태 포함)
            return ResponseEntity.ok(Map.of("success", true, "isFollowing", isNowFollowing));

        } catch (Exception e) {
            // 오류 발생 시 에러 메시지와 함께 500 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", e.getMessage()));
        }
    }


    /**
     * User reviews string.
     * 사용자별 리뷰 목록 페이지를 처리하는 컨트롤러 메서드
     *
     * @param userId      조회할 사용자의 ID
     * @param page        현재 페이지 번호 (기본값: 1)
     * @param starRating  별점 필터
     * @param sort        좋아요 순으로 정렬
     * @param currentUser 현재 로그인한 사용자 정보
     * @param model       the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/users/{userId}/reviews")
    public String userReviews(@PathVariable("userId") String userId,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "starRating", required = false) Integer starRating,
                              @RequestParam(value = "sort", required = false, defaultValue = "date") String sort,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              Model model) throws Exception {
        // 프로필 사용자 정보 조회
        UserDTO profileUser = socialService.selectUserProfile(userId);
        int followingCount = socialService.getFollowingCount(userId);
        int followerCount = socialService.getFollowerCount(userId);

        // 페이지네이션 정보 설정
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(page);

        // 총 리뷰 수 조회 및 총 페이지 수 계산
        int count;
        List<ReviewDTO> reviews;
        if ("likes".equals(sort)) {
            count = socialService.getReviewCountByUserIdSortedByLikes(userId, starRating);
            reviews = socialService.getReviewsByUserIdSortedByLikes(userId, pageDTO, starRating);
        } else {
            count = socialService.getReviewCountByUserId(userId, starRating);
            reviews = socialService.getReviewsByUserId(userId, pageDTO, starRating);
        }
        int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("reviews", reviews);
        model.addAttribute("count", count);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("starRating", starRating);
        model.addAttribute("sort", sort);

        boolean isOwnProfile = false;
        boolean isFollowing = false;

        // 현재 로그인한 사용자 정보 처리
        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);

            // 자신의 프로필인지 확인
            isOwnProfile = currentUserDTO.getUserId().equals(userId);

            // 다른 사용자의 프로필일 경우 팔로우 여부 확인
            if (!isOwnProfile) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
            }
        }

        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("isFollowing", isFollowing);

        return "social/user/reviews";
    }

    /**
     * Review detail string.
     * 리뷰 상세 페이지를 처리하는 컨트롤러 메서드
     *
     * @param reviewNo    조회할 리뷰의 번호
     * @param userId      리뷰 작성자의 ID
     * @param currentUser 현재 로그인한 사용자 정보
     * @param model       the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/users/{userId}/reviews/{reviewNo}")
    public String reviewDetail(@PathVariable("reviewNo") int reviewNo,
                               @PathVariable("userId") String userId,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               Model model) throws Exception {
        // 리뷰 조회수 증가
        socialService.incrementReviewViewCount(reviewNo);

        // 리뷰 정보 조회
        ReviewDTO review = socialService.getReviewByNo(reviewNo);

        // URL의 userId와 리뷰 작성자의 userId가 일치하는지 확인
        if (!review.getWriter().getUserId().equals(userId)) {
            // 일치하지 않을 경우 에러 페이지로 리다이렉트 또는 예외 처리
            throw new Exception("Invalid user ID for this review");
        }

        // 리뷰 작성자 정보 조회
        UserDTO profileUser = socialService.selectUserProfile(userId);
        int followingCount = socialService.getFollowingCount(userId);
        int followerCount = socialService.getFollowerCount(userId);

        model.addAttribute("review", review);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);

        boolean isOwnReview = false;
        boolean isFollowing = false;
        boolean isLiked = false;

        // 현재 로그인한 사용자 정보 처리
        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);

            // 자신의 리뷰인지 확인
            isOwnReview = currentUserDTO.getUserId().equals(userId);

            // 다른 사용자의 리뷰일 경우 팔로우 여부와 좋아요 여부 확인
            if (!isOwnReview) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
                isLiked = socialService.isReviewLikedByUser(reviewNo, currentUserDTO.getUserNo());
            }
        }

        model.addAttribute("isOwnReview", isOwnReview);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("isLiked", isLiked);

        return "social/review/detail";
    }

    /**
     * Show edit form string.
     * 리뷰 수정 페이지를 표시하는 컨트롤러 메서드
     *
     * @param reviewNo    수정할 리뷰의 번호
     * @param currentUser 현재 로그인한 사용자 정보
     * @param model       the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/reviews/{reviewNo}/edit")
    public String showEditForm(@PathVariable("reviewNo") int reviewNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               Model model) throws Exception {
        // 리뷰 정보 조회
        ReviewDTO review = socialService.getReviewByNo(reviewNo);

        // 리뷰가 존재하지 않거나 현재 사용자가 리뷰 작성자가 아닌 경우 접근 거부
        if (review == null || !review.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        // 현재 사용자(리뷰 작성자) 정보 조회
        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());

        model.addAttribute("review", review);
        model.addAttribute("profileUser", profileUser);
        return "social/review/edit";
    }

    /**
     * Update review string.
     * 리뷰를 수정하는 컨트롤러 메서드
     *
     * @param reviewNo    수정할 리뷰의 번호
     * @param reviewDTO   수정할 리뷰 정보를 담은 DTO
     * @param currentUser 현재 로그인한 사용자 정보
     * @return the string
     * @throws Exception the exception
     */
    @PostMapping("/reviews/{reviewNo}/edit")
    public String updateReview(@PathVariable("reviewNo") int reviewNo,
                               @ModelAttribute ReviewDTO reviewDTO,
                               @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        // 기존 리뷰 정보 조회
        ReviewDTO existingReview = socialService.getReviewByNo(reviewNo);

        // 리뷰가 존재하지 않거나 현재 사용자가 리뷰 작성자가 아닌 경우 접근 거부
        if (existingReview == null || !existingReview.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        // 수정할 리뷰 정보 설정
        reviewDTO.setReviewNo(reviewNo);
        reviewDTO.setWriter(currentUser.getUserDTO());  // 현재 로그인한 사용자 정보 설정
        // 리뷰 업데이트 서비스 호출
        socialService.updateReview(reviewDTO);
        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/reviews/" + reviewNo;
    }

    /**
     * Delete review string.
     * 리뷰를 삭제하는 컨트롤러 메서드
     *
     * @param reviewNo           삭제할 리뷰의 번호
     * @param currentUser        현재 로그인한 사용자 정보
     * @param redirectAttributes 리다이렉트 시 전달할 속성을 담는 객체
     * @return the string
     * @throws Exception the exception
     */
    @PostMapping("/reviews/{reviewNo}/delete")
    public String deleteReview(@PathVariable("reviewNo") int reviewNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               RedirectAttributes redirectAttributes) throws Exception {
        // 삭제할 리뷰 정보 조회
        ReviewDTO review = socialService.getReviewByNo(reviewNo);

        // 현재 사용자가 리뷰 작성자가 아닌 경우 접근 거부
        if (!review.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        // 리뷰 삭제 서비스 호출
        socialService.deleteReview(reviewNo);

        // 삭제 성공 메시지 설정
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/reviews";
    }

    /**
     * Toggle like response entity.
     * 리뷰 좋아요 토글 기능을 처리하는 컨트롤러 메서드
     *
     * @param reviewNo    좋아요를 토글할 리뷰의 번호
     * @param currentUser 현재 로그인한 사용자 정보
     * @return ResponseEntity<?>  좋아요 토글 결과와 최신 좋아요 수를 포함한 응답
     */
    @PostMapping("/reviews/{reviewNo}/like")
    @ResponseBody
    public ResponseEntity<?> toggleLike(@PathVariable("reviewNo") int reviewNo,
                                        @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            // 로그인 여부 확인
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "로그인이 필요합니다."));
            }

            // 리뷰 정보 조회
            ReviewDTO review = socialService.getReviewByNo(reviewNo);

            // 자신의 리뷰에 대한 좋아요 방지
            if (review.getWriter().getUserNo() == currentUser.getUserDTO().getUserNo()) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("error", "자신의 리뷰에는 좋아요를 할 수 없습니다."));
            }

            // 좋아요 토글 수행
            boolean isLiked = socialService.toggleReviewLike(reviewNo, currentUser.getUserDTO().getUserNo());

            // 좋아요 토글 후 최신 리뷰 정보 조회
            review = socialService.getReviewByNo(reviewNo);

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("isLiked", isLiked);
            response.put("likeCount", review.getLikeCount());

            // 성공 응답 반환
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 오류 발생 시 에러 메시지와 함께 500 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "오류가 발생했습니다."));
        }
    }

    /**
     * Show review insert form string.
     * 리뷰 작성 페이지를 표시하는 컨트롤러 메서드
     *
     * @param webtoonNo          리뷰를 작성할 웹툰의 번호
     * @param currentUser        현재 로그인한 사용자 정보
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/reviews/insert/{webtoonNo}")
    public String showReviewInsertForm(@PathVariable("webtoonNo") int webtoonNo,
                                       @AuthenticationPrincipal CustomUserDetails currentUser,
                                       Model model, RedirectAttributes redirectAttributes) throws Exception {
        // 웹툰 정보 조회
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        // 현재 사용자 프로필 정보 조회
        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());

        // 해당 사용자가 이미 이 웹툰에 대한 리뷰를 작성했는지 확인
        ReviewDTO existingReview = socialService.getReviewByUserAndWebtoon(currentUser.getUserDTO().getUserNo(), webtoonNo);
        if (existingReview != null) {
            // 이미 리뷰를 작성한 경우 에러 메시지를 추가하고 해당 리뷰 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("errorMessage", "이미 이 웹툰에 대한 리뷰를 작성하셨습니다.");
            return "redirect:/social/reviews/" + existingReview.getReviewNo();
        }

        // 새 리뷰 객체 생성 및 웹툰 정보 설정
        ReviewDTO review = new ReviewDTO();
        review.setWebtoon(webtoon);

        model.addAttribute("webtoon", webtoon);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("review", review);

        return "social/review/insert";
    }

    /**
     * Create review string.
     * 새로운 리뷰를 생성하는 컨트롤러 메서드
     *
     * @param review      사용자가 입력한 리뷰 정보를 담은 DTO
     * @param webtoonNo   리뷰 대상 웹툰의 번호
     * @param currentUser 현재 로그인한 사용자 정보
     * @return the string
     * @throws Exception the exception
     */
    @PostMapping("/reviews/insert")
    public String createReview(@ModelAttribute ReviewDTO review,
                               @RequestParam("webtoonNo") int webtoonNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        // 리뷰 작성자 정보 설정
        review.setWriter(currentUser.getUserDTO());

        // 리뷰 대상 웹툰 정보 조회 및 설정
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        review.setWebtoon(webtoon);

        // 리뷰 생성 서비스 호출
        socialService.createReview(review);
        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/reviews/" + review.getReviewNo();
    }

    /**
     * Show diary insert form string.
     * 다이어리 작성 페이지를 표시하는 컨트롤러 메서드
     *
     * @param webtoonNo   다이어리를 작성할 웹툰의 번호
     * @param currentUser 현재 로그인한 사용자 정보
     * @param model       the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/diaries/insert/{webtoonNo}")
    public String showDiaryInsertForm(@PathVariable("webtoonNo") int webtoonNo,
                                      @AuthenticationPrincipal CustomUserDetails currentUser,
                                      Model model) throws Exception {
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());

        DiaryDTO diary = new DiaryDTO();
        diary.setWebtoon(webtoon);

        model.addAttribute("webtoon", webtoon);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("diary", diary);

        return "social/diary/insert";
    }

    /**
     * Create diary string.
     * 새로운 다이어리를 생성하는 컨트롤러 메서드
     *
     * @param diary       사용자가 입력한 다이어리 정보를 담은 DTO
     * @param webtoonNo   다이어리 대상 웹툰의 번호
     * @param currentUser 현재 로그인한 사용자 정보
     * @return the string
     * @throws Exception the exception
     */
    @PostMapping("/diaries/insert")
    public String createDiary(@ModelAttribute DiaryDTO diary,
                              @RequestParam("webtoonNo") int webtoonNo,
                              @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        diary.setWriter(currentUser.getUserDTO());
        WebtoonDTO webtoon = socialService.getWebtoonByNo(webtoonNo);
        diary.setWebtoon(webtoon);

        int diaryNo = socialService.createDiary(diary);

        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/diaries/" + diary.getDiaryNo();
    }

    /**
     * User diaries string.
     * 사용자별 다이어리 목록 페이지를 표시하는 컨트롤러 메서드
     *
     * @param userId      조회할 사용자의 ID
     * @param page        현재 페이지 번호 (기본값: 1)
     * @param currentUser 현재 로그인한 사용자 정보
     * @param model       the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/users/{userId}/diaries")
    public String userDiaries(@PathVariable("userId") String userId,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              Model model) throws Exception {
        UserDTO profileUser = socialService.selectUserProfile(userId);
        int followingCount = socialService.getFollowingCount(userId);
        int followerCount = socialService.getFollowerCount(userId);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setStartEnd(page);

        int count = socialService.getDiaryCountByUserId(userId);
        int pages = count > 0 ? (int) Math.ceil((double) count / 10) : 1;

        List<DiaryDTO> diaries = socialService.getDiariesByUserId(userId, pageDTO);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("diaries", diaries);
        model.addAttribute("count", count);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);

        boolean isOwnProfile = false;
        boolean isFollowing = false;

        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);
            isOwnProfile = currentUserDTO.getUserId().equals(userId);

            if (!isOwnProfile) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
            }
        }

        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("isFollowing", isFollowing);

        return "social/user/diaries";
    }


    /**
     * Diary detail string.
     * 다이어리 상세 페이지를 표시하는 컨트롤러 메서드
     *
     * @param diaryNo     조회할 다이어리의 번호
     * @param currentUser 현재 로그인한 사용자 정보
     * @param model       the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/users/{userId}/diaries/{diaryNo}")
    public String diaryDetail(@PathVariable("userId") String userId,
                              @PathVariable("diaryNo") int diaryNo,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              Model model) throws Exception {
        socialService.incrementDiaryViewCount(diaryNo);

        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);

        // URL의 userId와 다이어리 작성자의 userId가 일치하는지 확인
        if (!diary.getWriter().getUserId().equals(userId)) {
            // 일치하지 않을 경우 에러 페이지로 리다이렉트 또는 예외 처리
            throw new Exception("Invalid user ID for this diary");
        }

        List<DiaryCommentDTO> comments = socialService.getDiaryComments(diaryNo);
        UserDTO profileUser = socialService.selectUserProfile(userId);
        int followingCount = socialService.getFollowingCount(userId);
        int followerCount = socialService.getFollowerCount(userId);

        model.addAttribute("diary", diary);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("comments", comments);

        boolean isOwnDiary = false;
        boolean isFollowing = false;

        if (currentUser != null) {
            UserDTO currentUserDTO = currentUser.getUserDTO();
            model.addAttribute("currentUser", currentUserDTO);
            isOwnDiary = currentUserDTO.getUserId().equals(userId);

            if (!isOwnDiary) {
                isFollowing = socialService.isFollowing(currentUserDTO.getUserNo(), profileUser.getUserNo());
            }
        }

        model.addAttribute("isOwnDiary", isOwnDiary);
        model.addAttribute("isFollowing", isFollowing);

        return "social/diary/detail";
    }

    /**
     * Show edit diary form string.
     * 다이어리 수정 페이지를 표시하는 컨트롤러 메서드
     *
     * @param diaryNo     수정할 다이어리의 번호
     * @param currentUser 현재 로그인한 사용자 정보
     * @param model       the model
     * @return the string
     * @throws Exception the exception
     */
    @GetMapping("/diaries/{diaryNo}/edit")
    public String showEditDiaryForm(@PathVariable("diaryNo") int diaryNo,
                                    @AuthenticationPrincipal CustomUserDetails currentUser,
                                    Model model) throws Exception {
        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
        if (diary == null || !diary.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        UserDTO profileUser = socialService.selectUserProfile(currentUser.getUserDTO().getUserId());
        model.addAttribute("diary", diary);
        model.addAttribute("profileUser", profileUser);
        return "social/diary/edit";
    }

    /**
     * Update diary string.
     * 다이어리를 수정하는 컨트롤러 메서드
     *
     * @param diaryNo     수정할 다이어리의 번호
     * @param diaryDTO    수정할 다이어리 정보를 담은 DTO
     * @param currentUser 현재 로그인한 사용자 정보
     * @return the string
     * @throws Exception the exception
     */
    @PostMapping("/diaries/{diaryNo}/edit")
    public String updateDiary(@PathVariable("diaryNo") int diaryNo,
                              @ModelAttribute DiaryDTO diaryDTO,
                              @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        DiaryDTO existingDiary = socialService.getDiaryByNo(diaryNo);
        if (existingDiary == null || !existingDiary.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        diaryDTO.setDiaryNo(diaryNo);
        diaryDTO.setWriter(currentUser.getUserDTO());
        socialService.updateDiary(diaryDTO);

        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/diaries/" + diaryNo;
    }

    /**
     * Delete diary string.
     * 다이어리를 삭제하는 컨트롤러 메서드
     *
     * @param diaryNo            삭제할 다이어리의 번호
     * @param currentUser        현재 로그인한 사용자 정보
     * @param redirectAttributes the redirect attributes
     * @return the string
     * @throws Exception the exception
     */
    @PostMapping("/diaries/{diaryNo}/delete")
    public String deleteDiary(@PathVariable("diaryNo") int diaryNo,
                               @AuthenticationPrincipal CustomUserDetails currentUser,
                               RedirectAttributes redirectAttributes) throws Exception {
        DiaryDTO diary = socialService.getDiaryByNo(diaryNo);
        if (!diary.getWriter().getUserId().equals(currentUser.getUserDTO().getUserId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        socialService.deleteDiary(diaryNo);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        return "redirect:/social/users/" + currentUser.getUserDTO().getUserId() + "/diaries";
    }

    /**
     * Gets diary comments.
     * 특정 다이어리의 댓글 목록을 조회하는 컨트롤러 메서드
     *
     * @param diaryNo 댓글을 조회할 다이어리의 번호
     * @return ResponseEntity<List < DiaryCommentDTO> > 댓글 목록을 포함한 응답
     */
    @GetMapping("/diaries/{diaryNo}/comments")
    @ResponseBody
    public ResponseEntity<List<DiaryCommentDTO>> getDiaryComments(@PathVariable int diaryNo) {
        try {
            // 서비스를 통해 해당 다이어리의 댓글 목록 조회
            List<DiaryCommentDTO> comments = socialService.getDiaryComments(diaryNo);
            // 성공적으로 조회된 경우 200 OK 상태와 함께 댓글 목록 반환
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error 상태와 함께 null 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Add comment response entity.
     * 다이어리에 새 댓글을 추가하는 컨트롤러 메서드
     *
     * @param diaryNo     댓글을 추가할 다이어리의 번호
     * @param payload     댓글 내용을 포함한 요청 본문
     * @param userDetails 현재 로그인한 사용자 정보
     * @return ResponseEntity<?>  댓글 추가 결과를 포함한 응답
     */
    @PostMapping("/diaries/{diaryNo}/comments")
    @ResponseBody
    public ResponseEntity<?> addComment(@PathVariable("diaryNo") int diaryNo,
                                        @RequestBody Map<String, String> payload,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            // 요청 본문에서 댓글 내용 추출
            String content = payload.get("content");
            // 서비스를 통해 새 댓글 추가
            DiaryCommentDTO comment = socialService.addDiaryComment(diaryNo, userDetails.getUserDTO().getUserNo(), content);
            // 성공적으로 추가된 경우 200 OK 상태와 함께 성공 메시지와 새 댓글 정보 반환
            return ResponseEntity.ok().body(Map.of("success", true, "comment", comment));
        } catch (Exception e) {
            // 예외 발생 시 400 Bad Request 상태와 함께 실패 메시지 반환
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * Delete comment response entity.
     * 다이어리 댓글을 삭제하는 컨트롤러 메서드
     *
     * @param commentNo   삭제할 댓글의 번호
     * @param userDetails 현재 로그인한 사용자 정보
     * @return ResponseEntity<?>  댓글 삭제 결과를 포함한 응답
     */
    @DeleteMapping("/diaries/comments/{commentNo}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable("commentNo") int commentNo,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            // 서비스를 통해 댓글 삭제 시도
            socialService.deleteDiaryComment(commentNo, userDetails.getUserDTO().getUserNo());
            // 성공적으로 삭제된 경우 200 OK 상태와 함께 성공 메시지 반환
            return ResponseEntity.ok(Map.of("success", true));
        } catch (AccessDeniedException e) {
            // 접근 권한이 없는 경우 403 Forbidden 상태와 함께 실패 메시지 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            // 기타 예외 발생 시 400 Bad Request 상태와 함께 실패 메시지 반환
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
