```
<select id="getSsdInventoryStock" resultMap="BaseResultMap"  parameterType="net.shopin.stock.domain.entity.SsdInventoryStock">
	    SELECT product_detail_sid ,supply_sid,shop_sid ,stock_type_sid,create_time FROM ssd_inventory_stock 
		where 1=1
		<if test="supplySid != null">
				and supply_sid = #{supplySid,jdbcType=BIGINT}
			</if>
			<if test="shopSid != null">
				and shop_sid = #{shopSid,jdbcType=BIGINT}
			</if>
		<if test="brandSid != null">
			and brand_sid =  #{brandSid,jdbcType=VARCHAR}
			</if>
			and flag = 0
	</select>
```
where 1=1 防止sql注入问题的发生;