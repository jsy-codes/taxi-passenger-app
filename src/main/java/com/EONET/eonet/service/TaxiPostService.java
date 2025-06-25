package com.EONET.eonet.service;

import org.json.JSONArray;
import org.json.JSONObject;
import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.repository.TaxiPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;



import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaxiPostService {

    private final TaxiPostRepository taxiPostRepository;
    private final String tmapKey = "L5YgKBB01c4hpq2CIQupG4yaczLAmZBaaohHMfD0"; // TMAP 앱키

    public int calculateFareFromTmap(double startLat, double startLon, double endLat, double endLon) {
        try {
            URL url = new URL("https://apis.openapi.sk.com/tmap/routes?version=1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("appKey", tmapKey);
            conn.setDoOutput(true);

            String body = """
                {
                  "startX": %f,
                  "startY": %f,
                  "endX": %f,
                  "endY": %f,
                  "reqCoordType": "WGS84GEO",
                  "resCoordType": "WGS84GEO",
                  "searchOption": 0
                }
            """.formatted(startLon, startLat, endLon, endLat);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject json = new JSONObject(response.toString());
            JSONArray features = json.getJSONArray("features");
            JSONObject properties = features.getJSONObject(0).getJSONObject("properties");
            return properties.getInt("taxiFare");

        } catch (Exception e) {
            e.printStackTrace();
            return 0; // 실패 시 기본값
        }
    }


    public TaxiPost getPostById(Long id) {
        return taxiPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    }

    public void deletePost(Long postId, String loginMemberId) {
        TaxiPost post = taxiPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        if (!post.getWriter().getId().equals(loginMemberId)) {
            throw new SecurityException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        taxiPostRepository.delete(post);
    }
    // 추가적인 기능(글쓰기, 수정, 삭제 등)도 여기서 작성 가능
}